package com.vert.mqtt;

import com.iot.core.device.DeviceOperation;
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
import java.util.function.Function;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 14:14
 */
public class MqttDeviceSession implements DeviceSession {
  final InternalLogger log = Log4JLoggerFactory.getInstance(MqttDeviceSession.class);

  //会话基础信息
  private MqttEndpoint endpoint;
  //设备协议
  private  ProtocolSupport protocolSupport;
  //设备操作的回掉
  private Function<String, DeviceOperation>operationSupplier;




  Long  lastPingTime  = System.currentTimeMillis();


  public MqttDeviceSession(MqttEndpoint mqttEndpoint, ProtocolSupport protocolSupport, Function<String, DeviceOperation>operationSupplier ) {
    this.endpoint = mqttEndpoint;
    this.protocolSupport =protocolSupport;
    this.operationSupplier =operationSupplier;
  }

  public MqttDeviceSession(MqttEndpoint endpoint) {
    this.endpoint = endpoint;
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
  public DeviceOperation operation() {
    return operationSupplier.apply(deviceId());
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
