package com.vert.session;

import com.iot.core.device.DeviceOperation;
import com.vert.core.ProtocolSupport;
import com.vert.message.codec.EncodedMessage;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 11:42
 */
public interface DeviceSession {

  String id();

  String deviceId();

  ProtocolSupport protocolSupport();
  DeviceOperation operation();
  void  send(EncodedMessage mqttMessage);

  void close();

  void ping();

  Boolean isAlive();
}
