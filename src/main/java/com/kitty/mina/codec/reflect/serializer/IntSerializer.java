
package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class IntSerializer extends Serializer
{
    @Override
    public Integer decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        return ByteBuffUtil.readInt(in);
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        if (value instanceof Short) {
            ByteBuffUtil.writeInt(out, ((Short) value).intValue());
        }
        else if (value instanceof Long) {
            ByteBuffUtil.writeInt(out, ((Long)value).intValue());
        }
        else if (value instanceof Byte) {
            ByteBuffUtil.writeInt(out, ((Byte) value).intValue());
        }
        else {
            ByteBuffUtil.writeInt(out, (int)value);
        }
    }
    
    public void encodeUnsignedShort(final IoBuffer out, final Object value, final Class<?> wrapper) {
        if (value instanceof Short) {
            ByteBuffUtil.writeUnsignedShort(out, ((Short) value).intValue());
        }
        else if (value instanceof Long) {
            ByteBuffUtil.writeUnsignedShort(out, ((Long)value).intValue());
        }
        else if (value instanceof Byte) {
            ByteBuffUtil.writeUnsignedShort(out, ((Byte) value).intValue());
        }
        else {
            ByteBuffUtil.writeUnsignedShort(out, (int)value);
        }
    }
}
