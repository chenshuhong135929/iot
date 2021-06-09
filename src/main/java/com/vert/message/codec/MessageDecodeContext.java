package com.vert.message.codec;

import com.vert.message.MessageCodecContext;

public interface MessageDecodeContext extends MessageCodecContext {
  EncodedMessage message();
}
