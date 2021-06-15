package com.vert.session;

import com.vert.message.DeviceMessageReply;

/**
 *
 * @Auther ChenShuHong
 * @Date 2021-06-08 11:37
 */
public interface DeviceSessionManager {

  /**
   * 根据设备ID或者会话ID获取设备会话
   *
   * @param idOrDeviceId 设备ID或者会话ID
   * @return 设备会话，不存在则返回 null
   */
  DeviceSession getSession(String idOrDeviceId );

  /**
   * 注册新设备会话，如果已经存在相同设备ID到会话，将注销旧的会话
   *
   * @param session 新的设备会话
   * @return 旧的设备会话，不存在则返回null
   */
  DeviceSession register(DeviceSession session );
  /**
   * 处理设备消息回复,当有设备上报了消息后,将调用此方法处理同步消息回复。
   *
   * @param session 设备会话
   * @param reply 上报的消息

   */
  void handleDeviceMessageReply(DeviceSession session  , DeviceMessageReply reply );

  /**
   * 使用会话ID或者设备ID注销设备会话
   *
   * @param idOrDeviceId 设备ID或者会话ID
   * @return 被注销的会话, 不存在则返回null
   */
  DeviceSession  unregister(String idOrDeviceId);




}
