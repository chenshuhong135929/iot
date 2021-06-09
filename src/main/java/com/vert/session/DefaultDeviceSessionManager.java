package com.vert.session;

import com.vert.message.DeviceMessage;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * 设备会话管理
 * @Auther ChenShuHong
 * @Date 2021-06-08 13:32
 */
public class DefaultDeviceSessionManager implements  DeviceSessionManager {

  ArrayDeque<Runnable> scheduleJobQueue ;
  //注册后的处理
  Consumer<DeviceSession> onDeviceRegister=(x)->{
    System.out.println("onDeviceRegister");
  };
  //注销后的处理
  Consumer<DeviceSession> onDeviceUnregister=(x)->{
    System.out.println("onDeviceUnregister");
  };
  final InternalLogger log = Log4JLoggerFactory.getInstance(DefaultDeviceSessionManager.class);
  Map<String, DeviceSession > repository = new  ConcurrentHashMap<>(4096);

  public void init(){


  }

  @Override
  public DeviceSession getSession(String idOrDeviceId) {
    DeviceSession session = repository.get(idOrDeviceId);
    if (session == null || !session.isAlive()) {
      return null;
    }
    return session;
  }

  @Override
  public DeviceSession register(DeviceSession session) {
    DeviceSession old = repository.put(session.deviceId(), session);
    log.info("当前注册的用户总量===== "+repository.size());
    if (old != null) {
      // 清空sessionId不同
      if ( old.id() != old.deviceId()) {
        repository.remove(old.id());
      }
    }
    if (session.id() != session.deviceId()) {
       session = repository.get(session.id());
    }
    if (null != old) {
      // 1. 可能是多个设备使用了相同的id.
      // 2. 可能是同一个设备,注销后立即上线,由于种种原因,先处理了上线后处理了注销逻辑.
      log.warn("注册的设备[{}]已存在,断开旧连接:{}", old.deviceId(), session);
      // 加入关闭连接队列
      scheduleJobQueue.add(()->{
        old.close();
      });

    } else {

    }
    // 通知
    onDeviceRegister.accept(session);
    return session;
  }

  @Override
  public void handleDeviceMessageReply(DeviceSession session, DeviceMessage reply) {

  }

  @Override
  public DeviceSession unregister(String idOrDeviceId) {
    DeviceSession client = repository.remove(idOrDeviceId);
    if (client != null) {
      if (client.id() != client.deviceId()) {
        if (client.id() == idOrDeviceId) {
          repository.remove(client.deviceId());
        } else {
          repository.remove(client.id());
        }
      }

      // 加入关闭连接队列
      scheduleJobQueue.add(()->{
        client.close();
      });

      // 通知
      onDeviceUnregister.accept(client);
    }

    return client;
  }


}
