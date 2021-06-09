package com.vert.message.codec;

import io.netty.buffer.ByteBuf;


/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 14:27
 */

public interface EncodedMessage {

  ByteBuf byteBuf();

  String deviceId();

}
