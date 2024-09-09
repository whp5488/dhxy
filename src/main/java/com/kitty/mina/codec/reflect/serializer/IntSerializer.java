package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class IntSerializer extends Serializer
{
    public IntSerializer() {
    }
    
    @Override
    public Integer decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        return Integer.valueOf(ByteBuffUtil.readInt(in));
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        if ((value instanceof Short)) {
            ByteBuffUtil.writeInt(out, (int)(Short)value);
        }
        else if ((value instanceof Long)) {
            ByteBuffUtil.writeInt(out, ((Long)value).intValue());
        }
        else if ((value instanceof Byte)) {
            ByteBuffUtil.writeInt(out, (int)(Byte)value);
        }
        else {
            ByteBuffUtil.writeInt(out, (int)value);
        }
    }
    
    public void encodeUnsignedShort(IoBuffer out, Object value, Class<?> wrapper) {
        if ((value instanceof Short)) {
            ByteBuffUtil.writeUnsignedShort(out, (int)(Short)value);
        }
        else if ((value instanceof Long)) {
            ByteBuffUtil.writeUnsignedShort(out, ((Long)value).intValue());
        }
        else if ((value instanceof Byte)) {
            ByteBuffUtil.writeUnsignedShort(out, (int)(Byte)value);
        }
        else {
            ByteBuffUtil.writeUnsignedShort(out, (int)value);
        }
    }
}
