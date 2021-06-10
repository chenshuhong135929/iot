package com.mqtt.start;

import com.iot.core.device.DeviceRegistryMqttCache;
import com.vert.session.DefaultDeviceSessionManager;

import io.vertx.core.Vertx;
import io.vertx.mqtt.MqttServerOptions;
import verticle.MqttVerticleServer;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-03 14:21
 */
public class MainLauncher  {

  public static void main(String[] args) {

   //
   /* MqttServerOptions mqttServerOptions =new MqttServerOptions();
    mqttServerOptions.setPort(1883);
    new MqttVerticleServer(new DefaultDeviceSessionManager(),mqttServerOptions);*/
 //   new MainLauncher().dispatch(new String[]{"run", MqttVerticleServer.class.getName()});
    Vertx vertx = Vertx.vertx();
    MqttServerOptions mqttServerOptions =new MqttServerOptions();
    mqttServerOptions.setPort(1883);
    DefaultDeviceSessionManager defaultDeviceSessionManager = new DefaultDeviceSessionManager();
    defaultDeviceSessionManager.init();
    vertx.deployVerticle( new MqttVerticleServer(defaultDeviceSessionManager,mqttServerOptions,new DeviceRegistryMqttCache()));
  }
  }
