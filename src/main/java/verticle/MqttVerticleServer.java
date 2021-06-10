package verticle;

import com.iot.core.device.DeviceOperation;
import com.iot.core.device.DeviceRegistry;
import com.vert.core.support.M2MIoTProtocolSupport;
import com.vert.message.DeviceMessage;
import com.vert.message.codec.EncodedMessage;
import com.vert.message.codec.FromDeviceMessageContext;
import com.vert.message.codec.Transport;
import com.vert.mqtt.MqttDeviceSession;
import com.vert.mqtt.VertxMqttMessage;
import com.vert.registry.AuthenticationResponse;
import com.vert.registry.MqttAuthenticationRequest;
import com.vert.session.DeviceSession;
import com.vert.session.DeviceSessionManager;
import io.netty.handler.codec.mqtt.MqttConnectReturnCode;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;
import io.vertx.mqtt.MqttServer;
import io.vertx.mqtt.MqttServerOptions;
import io.vertx.mqtt.MqttTopicSubscription;
import io.vertx.mqtt.messages.MqttPublishMessage;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @Auther ChenShuHong
 * @Date 2021-06-03 14:22
 */
public class MqttVerticleServer extends AbstractVerticle {


  final InternalLogger logger= Log4JLoggerFactory.getInstance(MqttVerticleServer.class);

  private DeviceSessionManager deviceSessionManager;
  private DeviceRegistry deviceRegistry;
  private MqttServerOptions mqttServerOptions;
  String publicServerAddress;
  public MqttVerticleServer(  DeviceSessionManager deviceSessionManager, MqttServerOptions mqttServerOptions,DeviceRegistry deviceRegistry) {
    this.mqttServerOptions=mqttServerOptions;
    this.deviceSessionManager = deviceSessionManager;
    this.deviceRegistry = deviceRegistry;
  }


  @Override
  public void start(Promise<Void> startPromise){
   /* if (publicServerAddress == null) {
      publicServerAddress =  mqttServerOptions.isSsl()==true? "ssl://" : "tcp://" + "127.0.0.1:"+mqttServerOptions.getPort()+"";
    }
*/
    MqttServer mqttServer = MqttServer.create(vertx,mqttServerOptions);
    mqttServer.endpointHandler(endpoint -> {

      logger.debug("客户端链接   [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());
      doAuth(endpoint).whenComplete((response,err)->{
        if (err != null) {
          logger.warn("设备认证[{}]失败:{}", getClientId(endpoint), err.getMessage(), err);
          //拒绝
          endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE);
        }else {

          logger.debug("成功=======================");
          accept(endpoint, new MqttDeviceSession(endpoint));
        }
      });

      // 接受来自远程客户端的连接
      endpoint.accept(false);
      endpoint.disconnectHandler(v -> {
        logger.debug("关闭客户端[{}]MQTT连接", endpoint.clientIdentifier());
        DeviceSession old = deviceSessionManager.unregister(endpoint.clientIdentifier());
        if (old == null) {
          if (endpoint.isConnected()) {
            endpoint.close();
          }
        }
      });

     /* //处理客户端订阅请求
      endpoint.subscribeHandler(subscribe -> {

        List<MqttQoS> grantedQosLevels = new ArrayList<>();
        for (MqttTopicSubscription s : subscribe.topicSubscriptions()) {
          logger.debug("订阅 " + s.topicName() + " 级别 QoS " + s.qualityOfService().value());
          grantedQosLevels.add(s.qualityOfService());
        }
        endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

      });*/
      //处理客户端取消订阅请求
      endpoint.unsubscribeHandler(unsubscribe -> {
        for (String t : unsubscribe.topics()) {
          logger.debug("取消订阅 " + t);
        }
        endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
      });

      //处理客户端发布的消息
      endpoint.publishHandler(message -> {
        System.out.println(endpoint.clientIdentifier());
        Function<String, DeviceOperation> operationSupplier =  x-> deviceRegistry.getDevice(x);
        handleMqttMessage( new MqttDeviceSession(endpoint,new M2MIoTProtocolSupport(),operationSupplier), endpoint, message);
        logger.debug("客户端topic:       "+message.topicName()+"客户端消息 [" + message.payload().toString(Charset.defaultCharset()) + " 级别 QoS [" + message.qosLevel() + "]");
        if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
          endpoint.publishAcknowledge(message.messageId());
        } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
          endpoint.publishReceived(message.messageId());
        }

        //向客户端发布消息
        endpoint.publish(message.topicName(),
            Buffer.buffer(" 来自服务器的回应" + message.payload().toString(Charset.defaultCharset())),
            MqttQoS.EXACTLY_ONCE,
            false,
            false);
      }).publishReleaseHandler(messageId -> {
        endpoint.publishComplete(messageId);
      });


      //（指定用于处理QoS 1和2的处理程序）specifing handlers for handling QoS 1 and 2
      endpoint.publishAcknowledgeHandler(messageId -> {
        logger.debug("Received ack for message （收到确认消息）= " + messageId);
      }).publishReceivedHandler(messageId -> {
        endpoint.publishRelease(messageId);
      }).publishCompletionHandler(messageId -> {
        logger.debug("Received ack for message （收到确认消息）= " + messageId);
      });

      //被客户通知保持生命
      endpoint.pingHandler(v -> {
        logger.debug("心跳信息。。。");
      });

    /*  mqttServer.close(v -> {
        logger.debug("（关闭服务器）MQTT server closed");
      });*/

    });


    //启动服务器
    mqttServer.listen( s -> {
      if (s.succeeded()) {
        logger.debug("开启服务器成功:{} success",s.result().actualPort());
      } else {
        logger.error("开启服务器失败 :{} error", s.cause());
      }
    });
    mqttServer.exceptionHandler(x->{
      System.out.println("异常");
    });


  }

  protected CompletionStage<AuthenticationResponse> doAuth(MqttEndpoint endpoint ){
    if (endpoint.auth() == null) {
      endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
      return CompletableFuture.completedFuture( AuthenticationResponse.error(401, "未提供认证信息"));
    }
    String clientId = getClientId(endpoint);
    String username = endpoint.auth().getUsername();
    String password = endpoint.auth().getPassword();
    //封装好验证信息
    MqttAuthenticationRequest mqttAuthenticationRequest = new MqttAuthenticationRequest(clientId, username, password);

    return
      CompletableFuture.completedFuture(AuthenticationResponse.success());

  }

  protected String  getClientId(MqttEndpoint endpoint) {
    return endpoint.clientIdentifier();
  }

  /**
   * 客户端订阅
   * @param endpoint
   * @param session
   */
  public void accept(MqttEndpoint endpoint ,MqttDeviceSession session){
    deviceSessionManager.register(session);
    logger.debug("MQTT客户端[{}]建立连接", session.deviceId());
    // 订阅请求
    endpoint.subscribeHandler(subscribe -> {
      List<MqttQoS> grantedQosLevels = subscribe.topicSubscriptions().stream().map(MqttTopicSubscription::qualityOfService).collect(Collectors.toList());
      if (logger.isDebugEnabled()) {
        subscribe.topicSubscriptions().stream().forEach(x->{
          logger.debug("SUBSCRIBE: [{}] 订阅topics {}  级别 QoS  {}  messageId={}", getClientId(endpoint), x.topicName(),x.qualityOfService().value() , subscribe.messageId());
        });

      }

          endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

       });
    }

    void handleMqttMessage(MqttDeviceSession  session , MqttEndpoint  endpoint , MqttPublishMessage message) {
    session.ping();
    String  deviceId = session.deviceId();
    // 设备推送了消息
      String  topicName = message.topicName();
      Buffer buffer = message.payload();
      if (logger.isDebugEnabled()) {
        logger.debug("收到设备[{}]消息[{}:{}]=>{}", deviceId, topicName, message.messageId(), buffer.toString());
    }
    try {

      VertxMqttMessage encodeMessage = new VertxMqttMessage(deviceId, message);

      // 转换消息
       DeviceMessage deviceMessage = decodeMessage(session, endpoint, encodeMessage);

      // 处理消息回复

        deviceSessionManager.handleDeviceMessageReply(session, deviceMessage);



    } catch (Throwable e ) {
      logger.error("处理设备[{}]消息[{}]:\n{}\n失败", deviceId, topicName, buffer.toString(), e);
    } finally {
      if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
        endpoint.publishAcknowledge(message.messageId());
      } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
        endpoint.publishReceived(message.messageId());
      }
    }
  }

  protected DeviceMessage decodeMessage(DeviceSession session, MqttEndpoint endpoint , VertxMqttMessage message ){
     String deviceId = message.deviceId();
    return  session.protocolSupport().messageCodec().decode(Transport.MQTT, new FromDeviceMessageContext() {
       @Override
       public void sendToDevice(EncodedMessage message) {
         session.send(message);
       }

       @Override
       public void disconnect() {
         doCloseEndpoint(endpoint, deviceId);
       }

       @Override
       public EncodedMessage message() {
         return message;
       }

       //
       @Override
       public DeviceOperation deviceOperation() {
         return session.operation();
       }
     });



  }

  protected void  doCloseEndpoint(MqttEndpoint client ,String  deviceId ) {
    logger.debug("关闭客户端[{}]MQTT连接", deviceId);
    DeviceSession old = deviceSessionManager.unregister(deviceId);

    if (old == null) {
      if (client.isConnected()) {
        client.close();
      }
    }
  }


  }
