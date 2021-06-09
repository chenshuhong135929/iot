package com.vert.registry;

import lombok.Data;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 16:49
 */
@Data
public class AuthenticationResponse {

  /**
   * 授权结果
   */
  Boolean success=true;

  /**
   * 授权状态码，意义同 http 状态码
   */
  int  code= 200;

  /**
   * 授权消息
   */
  String message= "授权通过";

  /**
   * 设备唯一ID
   */
  String  deviceId = null;


    /**
     * 授权成功
     *
     * @return
     */
    public static AuthenticationResponse  success()  {
      return new AuthenticationResponse();
    }

    /**
     * 授权成功
     *
     * @param deviceId 设备id
     * @return 授权响应
     */
    public static AuthenticationResponse  success(String deviceId ) {
      AuthenticationResponse  response =new  AuthenticationResponse();
      response.deviceId = deviceId;
      return response;
    }

    /**
     * 授权错误
     *
     * @param code 错误码
     * @param message 消息
     * @return 授权响应
     */
    public static AuthenticationResponse  error(int code, String message) {
      AuthenticationResponse response = new AuthenticationResponse();
      response.success = false;
      response.code = code;
      response.message = message;
      return response;
    }

}
