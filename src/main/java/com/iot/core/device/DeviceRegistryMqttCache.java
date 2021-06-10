package com.iot.core.device;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-10 11:11
 * 设备注册表，记录到缓存里面，缓存可以是redis或者其它数据库
 */
public class DeviceRegistryMqttCache implements DeviceRegistry {

  final InternalLogger logger= Log4JLoggerFactory.getInstance(DeviceRegistryMqttCache.class);

  @Override
  public DeviceOperation getDevice(String deviceId) {
    logger.debug("缓存注册表");
    return null;
  }

  @Override
  public String getProduct(String productId) {
    return null;
  }

  @Override
  public DeviceOperation registry(String deviceInfo) {
    return null;
  }

  @Override
  public void unregistry(String deviceId) {

  }
}
