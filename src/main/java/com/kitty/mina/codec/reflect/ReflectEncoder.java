package com.kitty.mina.codec.reflect;

import org.slf4j.LoggerFactory;
import com.kitty.mina.codec.reflect.serializer.Serializer;
import org.apache.mina.core.buffer.IoBuffer;
import com.kitty.mina.message.Message;
import org.slf4j.Logger;
import com.kitty.mina.codec.IMessageEncoder;

public class ReflectEncoder implements IMessageEncoder
{
    private static Logger logger;
    
    public ReflectEncoder() {
    }
    
    @Override
    public byte[] writeMessageBody(Message message) {
        IoBuffer out = IoBuffer.allocate(32).setAutoShrink(true).setAutoExpand(true);
        try {
            Serializer messageCodec = Serializer.getSerializer(message.getClass());
            messageCodec.encode(out, message, null);
        }
        catch (Exception e) {
            ReflectEncoder.logger.error("读取消息出错,模块号{}，类型{},异常{}", Integer.valueOf(message.getModule()), e);
        }
        out.flip();
        byte[] body = new byte[out.remaining()];
        out.get(body);
        return body;
    }
    
    static {
        ReflectEncoder.logger = LoggerFactory.getLogger(ReflectEncoder.class);
    }
}
