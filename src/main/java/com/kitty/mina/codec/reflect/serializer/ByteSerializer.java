
package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class ByteSerializer extends Serializer
{
    @Override
    public Byte decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        return ByteBuffUtil.readByte(in);
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        if (value instanceof Integer) {
            ByteBuffUtil.writeByte(out, ((Integer)value).byteValue());
        }
        else if (value instanceof Short) {
            ByteBuffUtil.writeByte(out, ((Short)value).byteValue());
        }
        else if (value instanceof Long) {
            ByteBuffUtil.writeByte(out, ((Long)value).byteValue());
        }
        else {
            ByteBuffUtil.writeByte(out, (byte)value);
        }
    }
}
