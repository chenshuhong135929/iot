package com.vert.core;

import com.iot.core.device.DeviceOperation;
import com.vert.message.codec.DeviceMessageCodec;
import com.vert.registry.AuthenticationRequest;
import com.vert.registry.AuthenticationResponse;

import java.util.concurrent.CompletionStage;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 14:36
 * 协议支持
 */
public interface ProtocolSupport  {

  /**
   * 协议ID
   * @return
   */
  String id();

  /**
   * 协议名称
   * @return
   */
  String name();


  /**
   * 协议说明
   * @return
   */
  String description();

  /**
   * 获取设备消息编码解码器
   * * 用于将平台统一的消息对象编码为设备的消息
   * * 用于将设备发送的消息转码为平台统一的消息对象
   *
   *  消息编解码器
   *
   */
  DeviceMessageCodec  messageCodec();

  /**
   * 进行设备认证
   * @param request
   * @param deviceOperation
   * @return
   */
  CompletionStage<AuthenticationResponse>authenticate(AuthenticationRequest request, DeviceOperation deviceOperation );
}
