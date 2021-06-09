package com.vert.registry;


import lombok.Data;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 17:04
 */
@Data
public class MqttAuthenticationRequest implements  AuthenticationRequest {
  /**
   * mqtt clientId
   */
  String clientId = null;
  /**
   * 用户名
   */
  String username = null;
  /**
   * 密码
   */
  String password = null;

  public MqttAuthenticationRequest(String clientId, String username, String password) {
    this.clientId = clientId;
    this.username = username;
    this.password = password;
  }
}
