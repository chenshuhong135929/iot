package com.vert.message.codec;

import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;

public interface TransportDeviceMessageCodec {

  Transport supportTransport();

  EncodedMessage  encode(MessageEncodeContext  context);

  DeviceMessage decode(MessageDecodeContext context);
}
