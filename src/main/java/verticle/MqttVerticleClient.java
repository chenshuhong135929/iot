package verticle;

import com.vert.message.DataMsg;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-03 14:24
 */
public class MqttVerticleClient {

  private String url = "localhost";
  private Integer port = 1883;

  public static void main(String[] args) {
    new MqttVerticleClient().startClien();
  }

  final InternalLogger logger = Log4JLoggerFactory.getInstance(MqttVerticleClient.class);

  public void startClien() {
    Vertx vertx = Vertx.vertx();
    MqttClientOptions mqttClientOptions = new MqttClientOptions();
    mqttClientOptions.setPassword("123456");
    mqttClientOptions.setUsername("123456");
    //甚至为设备ID然后服务器通过ID获取对应的话题进行返回
    mqttClientOptions.setClientId("设备1");
    MqttClient client = MqttClient.create(vertx, mqttClientOptions);
    client.connect(port, url, s -> {
      if (s.succeeded()) {
        logger.debug("链接服务器成功 :{} success", url + port);
        /*client.subscribe("userTopic", MqttQoS.EXACTLY_ONCE.value(),x->{
          logger.debug("订阅是否成功 :{} ", x.succeeded());
        });
*/
        AtomicInteger k = new AtomicInteger();
        CompletableFuture.runAsync(()->{
          while (true){
            try {
              Map<String, Object> map = new HashMap<>();
              map.put("header1","header111111");
              map.put("header2","header222222");

              JsonObject jsonObject =new JsonObject();
              jsonObject.put("messageId","小明");
              jsonObject.put("headers",map);
              jsonObject.put("deviceId","设备1");


              Thread.sleep(5000);
                client.publish("/read-property-reply", Buffer.buffer(jsonObject.toString()),MqttQoS.EXACTLY_ONCE,false,false,x->{
                logger.debug("发送订阅信息是否成功 :{} ", x.succeeded());
              });

              k.getAndIncrement();
            }catch (Exception e){
              logger.error(e);
            }

          }

        });
        //接收服务端信息
        client.publishHandler(message -> {
          logger.debug("(客户端消息) [" + message.payload().toString(Charset.defaultCharset()) + "] with QoS [" + message.qosLevel() + "]");
        } );
   /*     client.unsubscribe("topicOne",x->{
          logger.debug("取消订阅是否成功 :{} ", x.succeeded());
        });*/

      } else {
        logger.error("链接服务器失败 :{} error", s.cause());
      }
    });
    client.pingResponseHandler(s -> {
      logger.debug("接受客户端心跳。。。。。。");
    });

  }

}
