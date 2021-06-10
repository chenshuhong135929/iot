package com.vert.core.support;

import com.vert.message.codec.DefaultDeviceMessageCodec;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 17:54
 */
public class M2MIoTDeviceMessageCodec  extends DefaultDeviceMessageCodec {

  public M2MIoTDeviceMessageCodec() {
    register(new M2MIoTMQTTDeviceMessageCodec());
  }
}
