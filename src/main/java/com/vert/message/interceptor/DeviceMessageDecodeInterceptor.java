package com.vert.message.interceptor;

import com.vert.message.DeviceMessage;
import com.vert.message.codec.MessageDecodeContext;

/**
 * 设备消息编解码拦截器,在和设备通信过程中进行消息拦截
 */
public interface DeviceMessageDecodeInterceptor extends  DeviceMessageCodecInterceptor {

  /**
   * 解码前执行
   *
   * @param context
   */
  void  preDecode(MessageDecodeContext context);

  /**
   * 解码后执行
   *
   * @param context 消息上下文
   * @param deviceMessage 解码后的设备消息
   * @return 新的设备消息
   */
  DeviceMessage  postDecode(MessageDecodeContext context , DeviceMessage deviceMessage);
}
