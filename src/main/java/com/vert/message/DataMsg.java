package com.vert.message;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import org.omg.CORBA.Any;

import java.util.Map;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-10 16:14
 * 数据演示
 */
@Data
public class DataMsg  implements  DeviceMessage{


  @Override
  public String messageId() {
    return null;
  }

  @Override
  public String deviceId() {
    return null;
  }

  @Override
  public Long timestamp() {
    return null;
  }

  @Override
  public Map<String, Any> headers() {
    return null;
  }

  @Override
  public DeviceMessage addHeader(String header, Object value) {
    return null;
  }

  @Override
  public DeviceMessage removeHeader(String header) {
    return null;
  }

  @Override
  public JSONObject toJson() {
    return null;
  }

  @Override
  public void fromJson(JSONObject jsonObject) {

  }
}
