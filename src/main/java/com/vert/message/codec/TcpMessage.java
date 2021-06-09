package com.vert.message.codec;

import com.vert.message.codec.EncodedMessage;
import io.netty.buffer.ByteBuf;
import lombok.Data;

/**
 * @Auther ChenShuHong
 * @Date 2021-06-08 15:10
 */
@Data
public class TcpMessage implements EncodedMessage {
  @Override
  public ByteBuf byteBuf() {
    return null;
  }

  @Override
  public String deviceId() {
    return null;
  }
}
