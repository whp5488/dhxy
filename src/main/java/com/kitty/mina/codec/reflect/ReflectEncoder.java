package com.kitty.mina.codec.reflect;

import com.kitty.mina.codec.IMessageEncoder;
import com.kitty.mina.codec.reflect.serializer.Serializer;
import com.kitty.mina.message.Message;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectEncoder implements IMessageEncoder {
  private static Logger logger = LoggerFactory.getLogger(ReflectEncoder.class);
  
  public byte[] writeMessageBody(Message message) {
    IoBuffer out = IoBuffer.allocate(32).setAutoShrink(true).setAutoExpand(true);
    try {
      Serializer messageCodec = Serializer.getSerializer(message.getClass());
      messageCodec.encode(out, message, null);
    } catch (Exception e) {
      logger.error("读取消息出错,模块号{}，类型{},异常{}", Integer.valueOf(message.getModule()), e);
    } 
    out.flip();
    byte[] body = new byte[out.remaining()];
    out.get(body);
    return body;
  }
}
