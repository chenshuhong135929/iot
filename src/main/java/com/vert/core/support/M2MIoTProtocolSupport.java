package com.vert.core.support;

import cn.hutool.core.util.StrUtil;
import com.iot.core.device.DeviceOperation;
import com.vert.core.ProtocolSupport;
import com.vert.message.codec.DeviceMessageCodec;
import com.vert.registry.AuthenticationRequest;
import com.vert.registry.AuthenticationResponse;
import com.vert.registry.MqttAuthenticationRequest;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;


/**
 * @Auther ChenShuHong
 * @Date 2021-06-09 14:55
 */
public class M2MIoTProtocolSupport implements ProtocolSupport {
  @Override
  public String id() {
    return "m2miot-links";
  }

  @Override
  public String name() {
    return "M2MIoT Protocol 1.0";
  }

  @Override
  public String description() {
    return  "M2MIoT 协议";
  }

  @Override
  public DeviceMessageCodec messageCodec() {


    return new M2MIoTDeviceMessageCodec();
  }


  /**
   * 自定义认证协议
   * 用户名称        username  ：  SecureId |time|
   * 用户密码是签名  username|secureKey加密后的结果等于签名
   * @param request
   * @param deviceOperation
   * @return
   */
  @Override
  public CompletionStage<AuthenticationResponse> authenticate(AuthenticationRequest request, DeviceOperation deviceOperation) {
    if (request instanceof MqttAuthenticationRequest) {
      MqttAuthenticationRequest mqttAuthenticationRequest = (MqttAuthenticationRequest) request;
      String username = mqttAuthenticationRequest.getUsername();
      String password = mqttAuthenticationRequest.getPassword();
      if(StrUtil.isEmpty(username)){
            return CompletableFuture.completedFuture(AuthenticationResponse.error(401, "用户名不能为空"));
          }
          if(StrUtil.isEmpty(password)){
            return CompletableFuture.completedFuture(AuthenticationResponse.error(401, "密码不能为空"));
          }


          String requestSecureId ;
      try {
      String[] arr = username.split("|");

      if (arr.length <= 1) {
        return CompletableFuture.completedFuture(AuthenticationResponse.error(401, "用户名格式错误"));
      }
      requestSecureId = arr[0];
      long time  = Long.parseLong(arr[1]);
     // 和设备时间差大于5分钟则认为无效
      if (Math.abs(System.currentTimeMillis() - time) > TimeUnit.MINUTES.toMillis(5)) {
        return CompletableFuture.completedFuture(AuthenticationResponse.error(401, "设备时间不同步"));
      }

      deviceOperation.getAllAsync("secureId", "secureKey").thenAccept(conf->{

        String secureId = conf.get("secureId").toString();
        String secureKey = conf.get("secureKey").toString();
        String digest =  DigestUtils.md5Hex(username+"|"+secureKey);
        if (requestSecureId == secureId && digest == password) {
          AuthenticationResponse.success();
        } else {
          AuthenticationResponse.error(401, "密钥错误");
        }

      });
      } catch (NumberFormatException e) {
        return CompletableFuture.completedFuture(AuthenticationResponse.error(401, "用户名格式错误"));
      }

    }
    return CompletableFuture.completedFuture(AuthenticationResponse.error(400, "不支持的授权类型："+request));
  }
}
