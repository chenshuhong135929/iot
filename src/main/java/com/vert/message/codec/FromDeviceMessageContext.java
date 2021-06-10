package com.vert.message.codec;

public interface FromDeviceMessageContext extends  MessageDecodeContext{
  void sendToDevice(EncodedMessage message);
  void disconnect();
}
