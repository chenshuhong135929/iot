package com.vert.core.support;

import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;
import com.vert.message.codec.EncodedMessage;
import com.vert.message.codec.MessageDecodeContext;
import com.vert.message.codec.Transport;
import com.vert.message.codec.TransportDeviceMessageCodec;

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
    return null;
  }
}
