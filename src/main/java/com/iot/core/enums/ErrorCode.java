package com.iot.core.enums;

/**
 * 错误码定义
 */
public enum ErrorCode {

  /* 设备消息相关*/
  REQUEST_HANDLING("请求处理中"),
  CLIENT_OFFLINE("设备未在线"),
  NO_REPLY("设备未回复"),
  TIME_OUT("超时"),
  SYSTEM_ERROR("系统错误"),
  UNSUPPORTED_MESSAGE("不支持的消息"),
  PARAMETER_ERROR("参数错误"),
  PARAMETER_UNDEFINED("参数未定义"),
  FUNCTION_UNDEFINED("功能未定义"),
  PROPERTY_UNDEFINED("属性未定义")
  ;
  String text;

  ErrorCode(String text) {
    this.text = text;
  }
}
