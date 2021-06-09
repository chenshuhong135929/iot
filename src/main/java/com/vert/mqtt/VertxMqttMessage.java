package com.vert.mqtt;

import com.vert.message.codec.MqttMessage;
import io.netty.buffer.ByteBuf;
import io.vertx.mqtt.messages.MqttPublishMessage;
import lombok.Data;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 10:03
 * 封装获取到数据
 */
@Data
public class VertxMqttMessage extends MqttMessage {
private  String deviceId;
private MqttPublishMessage  message;

  public VertxMqttMessage(String deviceId, MqttPublishMessage message) {
    this.deviceId = deviceId;
    this.message = message;
  }

  @Override
  public String getTopic() {
    return message.topicName();
  }

  @Override
  public int getQosLevel() {
    return message.qosLevel().value();
  }

  @Override
  public Boolean getIsDup() {
    return message.isDup();
  }

  @Override
  public Boolean getIsRetai() {
    return message.isRetain();
  }

  @Override
  public ByteBuf getByteBuf() {
    return message.payload().getByteBuf();
  }
}
