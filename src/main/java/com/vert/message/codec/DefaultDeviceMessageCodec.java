package com.vert.message.codec;

import com.vert.message.DeviceMessage;
import com.vert.message.MessageEncodeContext;
import com.vert.message.interceptor.DeviceMessageCodecInterceptor;
import com.vert.message.interceptor.DeviceMessageDecodeInterceptor;
import com.vert.message.interceptor.DeviceMessageEncodeInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 16:20
 */
public class DefaultDeviceMessageCodec  implements  DeviceMessageCodec{

  Map<Transport, TransportDeviceMessageCodec > messageCodec = new ConcurrentHashMap<>();
  CopyOnWriteArrayList<DeviceMessageDecodeInterceptor> decodeDeviceMessageInterceptors = new CopyOnWriteArrayList();
  CopyOnWriteArrayList<DeviceMessageEncodeInterceptor> encodeDeviceMessageInterceptors = new CopyOnWriteArrayList();

  protected void  register(TransportDeviceMessageCodec codec) {
     messageCodec.put(codec.supportTransport(),codec)  ;
  }

  protected  void  register(DeviceMessageCodecInterceptor interceptor) {
    if (interceptor instanceof DeviceMessageDecodeInterceptor) {
      decodeDeviceMessageInterceptors.add((DeviceMessageDecodeInterceptor)interceptor);
    }
    if (interceptor instanceof DeviceMessageEncodeInterceptor) {
      encodeDeviceMessageInterceptors.add((DeviceMessageEncodeInterceptor)interceptor);
    }
  }

  @Override
  public EncodedMessage encode(Transport transport, MessageEncodeContext context) {
    return null;
  }

  @Override
  public DeviceMessage decode(Transport transport, MessageDecodeContext context) {
    return null;
  }
}
