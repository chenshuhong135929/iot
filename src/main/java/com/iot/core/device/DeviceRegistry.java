package com.iot.core.device;

public interface DeviceRegistry {
  /**
   * 获取设备操作接口, 请勿缓存返回值,注册中心已经实现本地缓存.
   * @param deviceId 设备ID
   * @return 设备操作接口
   */
  DeviceOperation getDevice(String deviceId );

  /**
   * 获取设备产品操作, 请勿缓存返回值,注册中心已经实现本地缓存.
   * @param productId 产品ID
   * @return 产品操作接口
   */
  //DeviceProductOperation  getProduct(String productId ):
  String   getProduct(String productId );
  /**
   * 注册设备，并返回设备操作接口, 请勿缓存返回值,注册中心已经实现本地缓存.
   * @param deviceInfo 设备基础信息
   * @return 设备操作接口
//  * @see getDevice
   */
 // DeviceOperation registry(DeviceInfo deviceInfo);
  DeviceOperation registry(String  deviceInfo);
  /**
   * 注销设备
   * @param deviceId 设备ID
   */
  void  unregistry(String deviceId );
}
