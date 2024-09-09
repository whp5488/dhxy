package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class BooleanSerializer extends Serializer
{
    public BooleanSerializer() {
    }
    
    @Override
    public Boolean decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        return Boolean.valueOf(ByteBuffUtil.readBoolean(in));
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeBoolean(out, (boolean)value);
    }
}
