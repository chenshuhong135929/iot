package com.iot.core.device;

import com.vert.core.Configurable;

import java.util.concurrent.CompletionStage;

public interface DeviceOperation  extends Configurable {
  /**
   * 设备id
   */
  String deviceId();


  /**
   * 当前设备连接所在的服务器ID,如果设备未上线 [DeviceState.online], 否则返回 [ null ]
   */
  String serverId();


  /**
   * 当前设备连接会话ID
   */
  String sessionId();

  /**
   *
   * @param state
   */
  void  putState(Byte state);


  /**
   * 获取当前状态
   *
   *
   */
  Byte  state();



  /**
   * 检查设备的真实状态
   *
   */
  CompletionStage<Byte>  checkState();





}
