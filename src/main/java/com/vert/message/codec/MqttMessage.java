package com.vert.message.codec;

import io.netty.buffer.ByteBuf;
import lombok.Data;

@Data
public class MqttMessage  implements EncodedMessage {

  String topic;

  int qosLevel;

  Boolean isDup;

  Boolean  isRetai;


  ByteBuf byteBuf;

  String deviceId;

  @Override
  public ByteBuf byteBuf() {
    return this.byteBuf;
  }

  @Override
  public String deviceId() {
    return this.deviceId;
  }
}
