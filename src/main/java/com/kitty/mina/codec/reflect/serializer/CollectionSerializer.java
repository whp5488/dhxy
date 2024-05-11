
package com.kitty.mina.codec.reflect.serializer;

import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Modifier;
import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class CollectionSerializer extends Serializer
{
    @Override
    public Object decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        final int size = ByteBuffUtil.readShort(in);
        return this.getObject(in, type, wrapper, size);
    }
    
    public Object decodeByte(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        final int size = ByteBuffUtil.readByte(in);
        return this.getObject(in, type, wrapper, size);
    }
    
    private Object getObject(final IoBuffer in, final Class<?> type, final Class<?> wrapper, final int size) {
        final int modifier = type.getModifiers();
        Collection<Object> result = null;
        if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
            if (List.class.isAssignableFrom(type)) {
                result = new ArrayList<Object>();
            }
            else if (Set.class.isAssignableFrom(type)) {
                result = new HashSet<Object>();
            }
        }
        else {
            try {
                result = (Collection)type.newInstance();
            }
            catch (final Exception e) {
                e.printStackTrace();
                result = new ArrayList<Object>();
            }
        }
        for (int i = 0; i < size; ++i) {
            final Serializer fieldCodec = Serializer.getSerializer(wrapper);
            final Object eleValue = fieldCodec.decode(in, wrapper, null);
            if (eleValue == null) {
                break;
            }
            result.add(eleValue);
        }
        return result;
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        if (value == null) {
            ByteBuffUtil.writeShort(out, (short)0);
            return;
        }
        final Collection<Object> collection = (Collection<Object>)value;
        final int size = collection.size();
        ByteBuffUtil.writeShort(out, (short)size);
        for (final Object elem : collection) {
            final Class<?> clazz = elem.getClass();
            final Serializer fieldCodec = Serializer.getSerializer(clazz);
            fieldCodec.encode(out, elem, wrapper);
        }
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
        final Collection<Object> collection = (Collection<Object>)value;
        final int size = collection.size();
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
        for (final Object elem : collection) {
            final Class<?> clazz = elem.getClass();
            final Serializer fieldCodec = Serializer.getSerializer(clazz);
            fieldCodec.encode(out, elem, wrapper);
        }
    }
}
