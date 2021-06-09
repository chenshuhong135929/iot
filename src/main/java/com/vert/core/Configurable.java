package com.vert.core;

import java.util.Map;
import java.util.concurrent.CompletionStage;

/**
 * 获取基础配置类进行授权鉴定根据不公协议获取不同配置
 */
public interface Configurable {


  /**
   * 异步获取全部配置
   *
   * @param key 配置key
   * @return value
   */
  CompletionStage<Map<String, Object>> getAllAsync(String... key );

}
