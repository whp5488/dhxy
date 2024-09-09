package com.kitty.mina.codec.reflect;

import org.slf4j.LoggerFactory;
import com.kitty.mina.codec.reflect.serializer.Serializer;
import com.kitty.mina.message.MessageFactory;
import org.apache.mina.core.buffer.IoBuffer;
import com.kitty.mina.message.Message;
import org.slf4j.Logger;
import com.kitty.mina.codec.IMessageDecoder;

public class ReflectDecoder implements IMessageDecoder
{
    private static Logger logger;
    
    public ReflectDecoder() {
    }
    
    @Override
    public Message readMessage(int module, int cmd, byte[] body) {
        IoBuffer in = IoBuffer.allocate(body.length);
        in.put(body);
        in.flip();
        Class<?> msgClazz = MessageFactory.INSTANCE.getMessage(module, cmd);
        if (msgClazz == null) {
            return null;
        }
        try {
            Serializer messageCodec = Serializer.getSerializer(msgClazz);
            return (Message)messageCodec.decode(in, msgClazz, null);
        }
        catch (Exception e) {
            ReflectDecoder.logger.error("读取消息出错,模块号{}，类型{},异常{}", new Object[] { Integer.valueOf(module), Integer.valueOf(cmd), e });
            return null;
        }
    }
    
    static {
        ReflectDecoder.logger = LoggerFactory.getLogger(ReflectDecoder.class);
    }
}
