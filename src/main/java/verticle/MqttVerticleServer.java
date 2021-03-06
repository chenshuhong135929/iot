package verticle;

import com.iot.core.device.DeviceOperation;
import com.iot.core.device.DeviceRegistry;
import com.vert.core.support.M2MIoTProtocolSupport;
import com.vert.message.DeviceMessage;
import com.vert.message.DeviceMessageReply;
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

      logger.debug("???????????????   [" + endpoint.clientIdentifier() + "] request to connect, clean session = " + endpoint.isCleanSession());
      doAuth(endpoint).whenComplete((response,err)->{
        if (err != null) {
          logger.warn("????????????[{}]??????:{}", getClientId(endpoint), err.getMessage(), err);
          //??????
          endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_SERVER_UNAVAILABLE);
        }else {

          logger.debug("??????=======================");
          accept(endpoint, new MqttDeviceSession(endpoint));
        }
      });

      // ????????????????????????????????????
      endpoint.accept(false);
      endpoint.disconnectHandler(v -> {
        logger.debug("???????????????[{}]MQTT??????", endpoint.clientIdentifier());
        DeviceSession old = deviceSessionManager.unregister(endpoint.clientIdentifier());
        if (old == null) {
          if (endpoint.isConnected()) {
            endpoint.close();
          }
        }
      });


      endpoint.unsubscribeHandler(unsubscribe -> {
        for (String t : unsubscribe.topics()) {
          logger.debug("???????????? " + t);
        }
        endpoint.unsubscribeAcknowledge(unsubscribe.messageId());
      });

      //??????????????????????????????
      endpoint.publishHandler(message -> {

        Function<String, DeviceOperation> operationSupplier =  x-> deviceRegistry.getDevice(x);
        handleMqttMessage( new MqttDeviceSession(endpoint,new M2MIoTProtocolSupport(),operationSupplier), endpoint, message);
        logger.debug("?????????topic:       "+message.topicName()+"??????????????? [" + message.payload().toString(Charset.defaultCharset()) + " ?????? QoS [" + message.qosLevel() + "]");
        if (message.qosLevel() == MqttQoS.AT_LEAST_ONCE) {
          endpoint.publishAcknowledge(message.messageId());
        } else if (message.qosLevel() == MqttQoS.EXACTLY_ONCE) {
          endpoint.publishReceived(message.messageId());
        }

        //????????????????????????
        endpoint.publish(message.topicName(),
            Buffer.buffer(" ????????????????????????" + message.payload().toString(Charset.defaultCharset())),
            MqttQoS.EXACTLY_ONCE,
            false,
            false);
      }).publishReleaseHandler(messageId -> {
        endpoint.publishComplete(messageId);
      });


      //?????????????????????QoS 1???2??????????????????specifing handlers for handling QoS 1 and 2
      endpoint.publishAcknowledgeHandler(messageId -> {
        logger.debug("Received ack for message ????????????????????????= " + messageId);
      }).publishReceivedHandler(messageId -> {
        endpoint.publishRelease(messageId);
      }).publishCompletionHandler(messageId -> {
        logger.debug("Received ack for message ????????????????????????= " + messageId);
      });

      //???????????????????????????
      endpoint.pingHandler(v -> {
        logger.debug("?????????????????????");
      });

    /*  mqttServer.close(v -> {
        logger.debug("?????????????????????MQTT server closed");
      });*/

    });


    //???????????????
    mqttServer.listen( s -> {
      if (s.succeeded()) {
        logger.debug("?????????????????????:{} success",s.result().actualPort());
      } else {
        logger.error("????????????????????? :{} error", s.cause());
      }
    });
    mqttServer.exceptionHandler(x->{
      System.out.println("??????");
    });


  }

  protected CompletionStage<AuthenticationResponse> doAuth(MqttEndpoint endpoint ){
    if (endpoint.auth() == null) {
      endpoint.reject(MqttConnectReturnCode.CONNECTION_REFUSED_NOT_AUTHORIZED);
      return CompletableFuture.completedFuture( AuthenticationResponse.error(401, "?????????????????????"));
    }
    String clientId = getClientId(endpoint);
    String username = endpoint.auth().getUsername();
    String password = endpoint.auth().getPassword();
    //?????????????????????
    MqttAuthenticationRequest mqttAuthenticationRequest = new MqttAuthenticationRequest(clientId, username, password);

    return
      CompletableFuture.completedFuture(AuthenticationResponse.success());

  }

  protected String  getClientId(MqttEndpoint endpoint) {
    return endpoint.clientIdentifier();
  }

  /**
   * ???????????????
   * @param endpoint
   * @param session
   */
  public void accept(MqttEndpoint endpoint ,MqttDeviceSession session){
    deviceSessionManager.register(session);
    logger.debug("MQTT?????????[{}]????????????", session.deviceId());
    // ????????????
    endpoint.subscribeHandler(subscribe -> {
      List<MqttQoS> grantedQosLevels = subscribe.topicSubscriptions().stream().map(MqttTopicSubscription::qualityOfService).collect(Collectors.toList());
      if (logger.isDebugEnabled()) {
        subscribe.topicSubscriptions().stream().forEach(x->{
          logger.debug("SUBSCRIBE: [{}] ??????topics {}  ?????? QoS  {}  messageId={}", getClientId(endpoint), x.topicName(),x.qualityOfService().value() , subscribe.messageId());
        });

      }

          endpoint.subscribeAcknowledge(subscribe.messageId(), grantedQosLevels);

       });
    }

    void handleMqttMessage(MqttDeviceSession  session , MqttEndpoint  endpoint , MqttPublishMessage message) {
    session.ping();
    String  deviceId = session.deviceId();
    // ?????????????????????
      String  topicName = message.topicName();
      Buffer buffer = message.payload();
      if (logger.isDebugEnabled()) {
        logger.debug("????????????[{}]??????[{}:{}]=>{}", deviceId, topicName, message.messageId(), buffer.toString());
    }
    try {

      VertxMqttMessage encodeMessage = new VertxMqttMessage(deviceId, message);


      // ????????????
       DeviceMessage deviceMessage = decodeMessage(session, endpoint, encodeMessage);



      // ??????????????????
      if(deviceMessage instanceof DeviceMessageReply ){
        DeviceMessageReply  deviceMessageReply =(DeviceMessageReply) deviceMessage;
        deviceSessionManager.handleDeviceMessageReply(session, deviceMessageReply);
      }






    } catch (Throwable e ) {
      logger.error("????????????[{}]??????[{}]:\n{}\n??????", deviceId, topicName, buffer.toString(), e);
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
    logger.debug("???????????????[{}]MQTT??????", deviceId);
    DeviceSession old = deviceSessionManager.unregister(deviceId);

    if (old == null) {
      if (client.isConnected()) {
        client.close();
      }
    }
  }


  }
