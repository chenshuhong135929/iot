package com.vert.message.codec;

import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;

public interface TransportDeviceMessageCodec {

  Transport supportTransport();
  //编码
  EncodedMessage  encode(MessageEncodeContext  context);
 //解码
  DeviceMessage decode(MessageDecodeContext context);
}
