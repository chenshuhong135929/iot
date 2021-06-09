package com.iot.core.metadata;

import com.alibaba.fastjson.JSONObject;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 10:15
 */
public interface Jsonable {
  JSONObject  toJson();
  void fromJson(JSONObject jsonObject);

}
