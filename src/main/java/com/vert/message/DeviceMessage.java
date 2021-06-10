package com.vert.message;

import com.iot.core.metadata.Jsonable;
import org.omg.CORBA.Any;

import java.io.Serializable;
import java.util.Map;

public interface DeviceMessage extends Jsonable , Serializable {
  String messageId();
  String deviceId();
  Long timestamp();

  Map<String, Any> headers();

  /**
   * 添加一个header
   * @param header
   * @param value
   * @return
   */
  DeviceMessage  addHeader(String header , Object value);

  /**
   * 删除一个header
   * @param header
   * @return
   */
  DeviceMessage  removeHeader(String header );

  default  Object getHeader(String header) {
    return headers().get(header);
  }
}
