package com.vert.message;

import com.iot.core.enums.ErrorCode;

public interface DeviceMessageReply extends     DeviceMessage{


  String  code();

  String message();

  DeviceMessageReply error(ErrorCode errorCode);

  DeviceMessageReply error(Throwable e);

  DeviceMessageReply deviceId(String deviceId);

  DeviceMessageReply success();

  DeviceMessageReply code(String code);

  DeviceMessageReply  message(String message);

  DeviceMessageReply  from(DeviceMessage message);

  DeviceMessageReply  messageId(String messageId);
}
