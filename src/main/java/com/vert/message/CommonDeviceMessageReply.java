package com.vert.message;

import com.alibaba.fastjson.JSONObject;
import com.iot.core.enums.ErrorCode;

import java.util.Map;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-11 14:45
 */
public class CommonDeviceMessageReply  implements  DeviceMessageReply {
  @Override
  public String code() {
    return null;
  }

  @Override
  public String message() {
    return null;
  }

  @Override
  public DeviceMessageReply error(ErrorCode errorCode) {
    return null;
  }

  @Override
  public DeviceMessageReply error(Throwable e) {
    return null;
  }

  @Override
  public DeviceMessageReply deviceId(String deviceId) {
    return null;
  }

  @Override
  public String messageId() {
    return null;
  }

  @Override
  public String deviceId() {
    return null;
  }

  @Override
  public Long timestamp() {
    return null;
  }

  @Override
  public Map<String, Object> headers() {
    return null;
  }

  @Override
  public DeviceMessage addHeader(String header, Object value) {
    return null;
  }

  @Override
  public DeviceMessage removeHeader(String header) {
    return null;
  }

  @Override
  public DeviceMessageReply success() {
    return null;
  }

  @Override
  public DeviceMessageReply code(String code) {
    return null;
  }

  @Override
  public DeviceMessageReply message(String message) {
    return null;
  }

  @Override
  public DeviceMessageReply from(DeviceMessage message) {
    return null;
  }

  @Override
  public DeviceMessageReply messageId(String messageId) {
    return null;
  }

  @Override
  public JSONObject toJson() {
    return null;
  }

  @Override
  public void fromJson(JSONObject jsonObject) {

  }
}
