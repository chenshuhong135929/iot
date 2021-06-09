package com.vert.mqtt;

import com.vert.core.ProtocolSupport;
import com.vert.message.codec.EncodedMessage;
import com.vert.message.codec.MqttMessage;
import com.vert.session.DeviceSession;
import io.netty.handler.codec.mqtt.MqttQoS;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.buffer.Buffer;
import io.vertx.mqtt.MqttEndpoint;

import java.nio.charset.StandardCharsets;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 14:14
 */
public class MqttDeviceSession implements DeviceSession {
  final InternalLogger log = Log4JLoggerFactory.getInstance(MqttDeviceSession.class);


  private MqttEndpoint endpoint;
  private  ProtocolSupport protocolSupport;
  Long  lastPingTime  = System.currentTimeMillis();
  public MqttDeviceSession(MqttEndpoint mqttEndpoint,ProtocolSupport protocolSupport) {
    this.endpoint = mqttEndpoint;
    this.protocolSupport =protocolSupport;
  }

  @Override
  public String id() {
    return deviceId();
  }

  @Override
  public String deviceId() {
    return  endpoint.clientIdentifier();
  }

  @Override
  public ProtocolSupport protocolSupport() {

    return protocolSupport;
  }


  @Override
  public void send(EncodedMessage encodedMessage) {
    if (encodedMessage instanceof MqttMessage) {
      MqttMessage mqttMessage= (MqttMessage) encodedMessage;
      ping();
      Buffer buffer = Buffer.buffer(encodedMessage.byteBuf());
      if (log.isDebugEnabled()) {
        log.debug("发送消息到MQTT客户端[{}]=>[{}]:{}", mqttMessage.getTopic(), deviceId(), buffer.toString(StandardCharsets.UTF_8));
      }
      endpoint.publish(mqttMessage.getTopic(), buffer, MqttQoS.valueOf(mqttMessage.getQosLevel()), mqttMessage.getIsDup(), mqttMessage.getIsRetai());
    } else {
      log.error("不支持发送消息{}到MQTT:", encodedMessage);
    }
  }

  @Override
  public void close() {
    try {
      if (endpoint.isConnected()) {

        endpoint.close();
      }
    } catch ( Exception e) {
    }
  }

  @Override
  public void ping() {
    lastPingTime = System.currentTimeMillis();
  }

  @Override
  public Boolean isAlive() {
    return null;
  }
}
