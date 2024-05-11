
package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class ShortSerializer extends Serializer
{
    @Override
    public Short decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        return ByteBuffUtil.readShort(in);
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        if (value instanceof Long) {
            ByteBuffUtil.writeShort(out, ((Long)value).shortValue());
        }
        else if (value instanceof Integer) {
            ByteBuffUtil.writeShort(out, ((Integer)value).shortValue());
        }
        else if (value instanceof Byte) {
            ByteBuffUtil.writeShort(out, (short)value);
        }
        else {
            ByteBuffUtil.writeShort(out, (short)value);
        }
    }
    
    public void encodeUnsignedByte(final IoBuffer out, Object value, final Class<?> wrapper) {
        if (value instanceof Long) {
            ByteBuffUtil.writeUnsignedByte(out, ((Long)value).shortValue());
        }
        else if (value instanceof Integer) {
            ByteBuffUtil.writeUnsignedByte(out, ((Integer)value).shortValue());
        }
        else if (value instanceof Byte) {
            value = value;
            ByteBuffUtil.writeUnsignedByte(out, (short)value);
        }
        else {
            ByteBuffUtil.writeUnsignedByte(out, (short)value);
        }
    }
}
