package com.vert.core.support;

import com.alibaba.fastjson.JSONObject;
import com.vert.message.DataMsg;
import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;
import com.vert.message.codec.*;

import lombok.Data;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-10 10:13
 */
public class M2MIoTMQTTDeviceMessageCodec implements TransportDeviceMessageCodec {
  @Override
  public Transport supportTransport() {
    return Transport.MQTT;
  }

  @Override
  public EncodedMessage encode(MessageEncodeContext context) {

    return null;
  }

  @Override
  public DeviceMessage decode(MessageDecodeContext context) {
    MqttMessage message=(MqttMessage) context.message();
    String topic = message.getTopic();

    return null;
  }

  @Data
  class  EncodeResult{
    String topic;
    JSONObject data;
  }

  public DecodeResult decode(String topic ,JSONObject msg){
    DecodeResult result = new DecodeResult(topic);
    switch (topic){
      case "/read-property-reply":
        result.message=msg.toJavaObject(DataMsg.class);
        break;

    }
    return result;
  }

  class DecodeResult  {
    String topic;
    DeviceMessage  message;

    public DecodeResult(String topic) {
      this.topic = topic;
    }
  }

}
