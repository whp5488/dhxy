package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class LongSerializer extends Serializer
{
    public LongSerializer() {
    }
    
    @Override
    public Long decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        return Long.valueOf(ByteBuffUtil.readLong(in));
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeLong(out, (long)value);
    }
    
    public void encodeUnsignedInt(IoBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeUnsignedInt(out, (long)value);
    }
}
