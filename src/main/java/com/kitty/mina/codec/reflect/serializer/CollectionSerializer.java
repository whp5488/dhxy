package com.kitty.mina.codec.reflect.serializer;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.lang.reflect.Modifier;
import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class CollectionSerializer extends Serializer
{
    public CollectionSerializer() {
    }
    
    @Override
    public Object decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        int size = ByteBuffUtil.readShort(in);
        return this.getObject(in, type, wrapper, size);
    }
    
    public Object decodeByte(IoBuffer in, Class<?> type, Class<?> wrapper) {
        int size = ByteBuffUtil.readByte(in);
        return this.getObject(in, type, wrapper, size);
    }
    
    private Object getObject(IoBuffer in, Class<?> type, Class<?> wrapper, int size) {
        int modifier = type.getModifiers();
        Collection<Object> result = null;
        if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
            if (List.class.isAssignableFrom(type)) {
                result = new ArrayList<>();
            }
            else if (Set.class.isAssignableFrom(type)) {
                result = new HashSet();
            }
        }
        else {
            try {
                result = (Collection)type.newInstance();
            }
            catch (Exception e) {
                e.printStackTrace();
                result = new ArrayList<>();
            }
        }
        int i = 0;
        while (i < size) {
            Serializer fieldCodec = Serializer.getSerializer(wrapper);
            Object eleValue = fieldCodec.decode(in, wrapper, null);
            if (eleValue == null) {
                break;
            }
            result.add(eleValue);
            ++i;
        }
        return result;
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        if (value == null) {
            ByteBuffUtil.writeShort(out, (short)0);
            return;
        }
        Collection<Object> collection = (Collection)value;
        int size = collection.size();
        ByteBuffUtil.writeShort(out, (short)size);
        for (Object elem : collection) {
            Class<?> clazz = elem.getClass();
            Serializer fieldCodec = Serializer.getSerializer(clazz);
            fieldCodec.encode(out, elem, wrapper);
        }
        return;
    }
    
    public void encode(IoBuffer out, Object value, Class<?> wrapper, int type) {
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
        else {
            Collection<Object> collection = (Collection)value;
            int size = collection.size();
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
            for (Object elem : collection) {
                Class<?> clazz = elem.getClass();
                Serializer fieldCodec = Serializer.getSerializer(clazz);
                fieldCodec.encode(out, elem, wrapper);
            }
            return;
        }
    }
}
