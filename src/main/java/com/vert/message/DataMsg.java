package com.vert.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import java.util.Map;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-10 16:14
 * 数据演示
 */
@Data
public class DataMsg  implements  DeviceMessage{



  private  String messageId;

  private  String deviceId;

  private  String code;

  private   Map<String, Object> headers;

  @Override
  public String messageId() {
    return this.messageId;
  }

  @Override
  public String deviceId() {
    return this.deviceId;
  }

  @Override
  public Long timestamp() {
    return null;
  }

  @Override
  public Map<String, Object> headers() {
    return headers;
  }

  @Override
  public DeviceMessage addHeader(String header, Object value) {
    headers.put(header,value);
    return this;
  }

  @Override
  public DeviceMessage removeHeader(String header) {
    headers.remove(header);
    return this;
  }

  @Override
  public JSONObject toJson() {
    return (JSONObject) JSON.toJSON(this);
  }

  @Override
  public void fromJson(JSONObject jsonObject) {
    deviceId = jsonObject.getString("deviceId");
    messageId = jsonObject.getString("messageId");
    code = jsonObject.getString("code");
    this.headers = jsonObject.getJSONObject("headers");
  }
}
