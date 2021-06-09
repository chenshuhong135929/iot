package com.vert.core;

import com.iot.core.device.DeviceOperation;
import com.vert.registry.AuthenticationRequest;
import com.vert.registry.AuthenticationResponse;

import java.util.concurrent.CompletionStage;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 14:36
 * 协议支持
 */
public interface ProtocolSupport {

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
   * 进行设备认证
   * @param request
   * @param deviceOperation
   * @return
   */
  CompletionStage<AuthenticationResponse>authenticate(AuthenticationRequest request, DeviceOperation deviceOperation );
}
