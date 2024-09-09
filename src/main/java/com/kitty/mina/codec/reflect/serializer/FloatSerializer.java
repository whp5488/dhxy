package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class FloatSerializer extends Serializer
{
    public FloatSerializer() {
    }
    
    @Override
    public Float decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        return Float.valueOf(ByteBuffUtil.readFloat(in));
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeFloat(out, (float)value);
    }
}
