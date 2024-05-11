
package com.kitty.mina.codec.reflect.serializer;

import java.lang.reflect.Array;
import com.kitty.common.utils.ReflectUtil;
import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class ArraySerializer extends Serializer
{
    @Override
    public Object decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        final int size = ByteBuffUtil.readShort(in);
        final Object array = ReflectUtil.newArray(type, wrapper, size);
        for (int i = 0; i < size; ++i) {
            final Serializer fieldCodec = Serializer.getSerializer(wrapper);
            final Object eleValue = fieldCodec.decode(in, wrapper, null);
            Array.set(array, i, eleValue);
        }
        return array;
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        if (value == null) {
            ByteBuffUtil.writeShort(out, (short)0);
            return;
        }
        final int size = Array.getLength(value);
        ByteBuffUtil.writeShort(out, (short)size);
        this.encodeObject(out, value, wrapper, size);
    }
    
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper, final int type) {
        if (value == null) {
            if (type == 1) {
                ByteBuffUtil.writeByte(out, (byte)0);
            }
            else if (type != 2) {
                if (type == 3) {
                    ByteBuffUtil.writeInt(out, 0);
                }
                else {
                    ByteBuffUtil.writeShort(out, (short)0);
                }
            }
            return;
        }
        final int size = Array.getLength(value);
        if (type == 1) {
            ByteBuffUtil.writeByte(out, (byte)size);
        }
        else if (type != 2) {
            if (type == 3) {
                ByteBuffUtil.writeInt(out, size);
            }
            else {
                ByteBuffUtil.writeShort(out, (short)size);
            }
        }
        this.encodeObject(out, value, wrapper, size);
    }
    
    private void encodeObject(final IoBuffer out, final Object value, final Class<?> wrapper, final int size) {
        for (int i = 0; i < size; ++i) {
            final Object elem = Array.get(value, i);
            final Class<?> clazz = elem.getClass();
            final Serializer fieldCodec = Serializer.getSerializer(clazz);
            fieldCodec.encode(out, elem, wrapper);
        }
    }
}
