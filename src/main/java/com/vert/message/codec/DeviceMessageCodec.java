package com.vert.message.codec;

import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;

/**
 * 设备消息转换器，用于对不同协议的消息进行转换
 */
public interface DeviceMessageCodec {

  /**
   * 编码，将消息进行编码，用于发送到设备
   *
   * @param transport 传输协议
   * @param context 消息上下文
   * @return 编码结果
   * @see MqttMessage
   * @see
   */
  EncodedMessage encode(Transport transport, MessageEncodeContext context);

  /**
   * 解码，用于将收到设备上传的消息解码为可读的消息
   * @param transport 传输协议
   * @param context 消息上下文
   * @return 解码结果
   */
  DeviceMessage decode(Transport transport ,MessageDecodeContext  context );
}
