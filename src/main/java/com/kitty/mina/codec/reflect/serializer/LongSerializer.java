
package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class LongSerializer extends Serializer
{
    @Override
    public Long decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        return ByteBuffUtil.readLong(in);
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        ByteBuffUtil.writeLong(out, (long)value);
    }
    
    public void encodeUnsignedInt(final IoBuffer out, final Object value, final Class<?> wrapper) {
        ByteBuffUtil.writeUnsignedInt(out, (long)value);
    }
}
