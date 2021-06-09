package com.vert.message.codec;

import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;
import com.vert.message.interceptor.DeviceMessageDecodeInterceptor;
import com.vert.message.interceptor.DeviceMessageEncodeInterceptor;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 16:20
 */
public class DefaultDeviceMessageCodec  implements  DeviceMessageCodec{


  CopyOnWriteArrayList<DeviceMessageDecodeInterceptor> decodeDeviceMessageInterceptors = new CopyOnWriteArrayList();
  CopyOnWriteArrayList<DeviceMessageEncodeInterceptor> encodeDeviceMessageInterceptors = new CopyOnWriteArrayList();


  @Override
  public EncodedMessage encode(Transport transport, MessageEncodeContext context) {
    return null;
  }

  @Override
  public DeviceMessage decode(Transport transport, MessageDecodeContext context) {
    return null;
  }
}
