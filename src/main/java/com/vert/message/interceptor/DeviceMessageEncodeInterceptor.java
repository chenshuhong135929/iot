package com.vert.message.interceptor;

import com.vert.message.MessageEncodeContext;
import com.vert.message.codec.EncodedMessage;

/**
 * 设备消息解码拦截器,用于在对消息进行编码时进行自定义处理
 */
public interface DeviceMessageEncodeInterceptor  extends  DeviceMessageCodecInterceptor{
  /**
   * 编码前执行
   *
   * @param context 编码上下文
   */
  void  preEncode(MessageEncodeContext context );

  /**
   * 编码后执行
   *
   * @param context 编码上下文
   * @param message 已编码的消息
   * @return 新的消息
   */
  EncodedMessage postEncode(MessageEncodeContext context , EncodedMessage message);

}
