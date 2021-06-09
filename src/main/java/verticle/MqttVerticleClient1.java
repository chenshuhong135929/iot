package verticle;

import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttClient;
import io.vertx.mqtt.MqttClientOptions;

import java.nio.charset.Charset;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-03 14:24
 */
public class MqttVerticleClient1 {

  private String url = "localhost";
  private Integer port = 1883;

  public static void main(String[] args) {
    new MqttVerticleClient1().startClien();
  }

  final InternalLogger logger = Log4JLoggerFactory.getInstance(MqttVerticleClient1.class);

  public void startClien() {
    Vertx vertx = Vertx.vertx();
    MqttClientOptions mqttClientOptions = new MqttClientOptions();
    mqttClientOptions.setPassword("123456");
    mqttClientOptions.setUsername("123456");
    mqttClientOptions.setClientId("设备2");
    MqttClient client = MqttClient.create(vertx, mqttClientOptions);

    client.connect(port, url, s -> {
      if (s.succeeded()) {
        logger.debug("链接服务器成功 :{} success", url + port);
        client.subscribe("topicOne", MqttQoS.EXACTLY_ONCE.value(),x->{
          logger.debug("订阅是否成功 :{} ", x.succeeded());
        });

        client.publishHandler(message -> {
          logger.debug("(客户端消息) [" + message.payload().toString(Charset.defaultCharset()) + "] with QoS [" + message.qosLevel() + "]");
        } );
      } else {
        logger.error("链接服务器失败 :{} error", s.cause());
      }
    });
    client.pingResponseHandler(s -> {
      logger.debug("接受客户端心跳。。。。。。");
    });

  }

}
