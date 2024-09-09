package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class DoubleSerializer extends Serializer
{
    public DoubleSerializer() {
    }
    
    @Override
    public Double decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        return Double.valueOf(ByteBuffUtil.readDouble(in));
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeDouble(out, (double)value);
    }
}
