package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.mina.core.buffer.IoBuffer;

public class CollectionSerializer extends Serializer {
  public Object decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    int size = ByteBuffUtil.readShort(in);
    return getObject(in, type, wrapper, size);
  }
  
  public Object decodeByte(IoBuffer in, Class<?> type, Class<?> wrapper) {
    int size = ByteBuffUtil.readByte(in);
    return getObject(in, type, wrapper, size);
  }
  
  private Object getObject(IoBuffer in, Class<?> type, Class<?> wrapper, int size) {
    int modifier = type.getModifiers();
    Collection<Object> result = null;
    if (Modifier.isAbstract(modifier) || Modifier.isInterface(modifier)) {
      if (List.class.isAssignableFrom(type)) {
        result = new ArrayList();
      } else if (Set.class.isAssignableFrom(type)) {
        result = new HashSet();
      } 
    } else {
      try {
        result = (Collection<Object>)type.newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        result = new ArrayList();
      } 
    } 
    for (int i = 0; i < size; i++) {
      Serializer fieldCodec = Serializer.getSerializer(wrapper);
      Object eleValue = fieldCodec.decode(in, wrapper, null);
      if (eleValue == null)
        break; 
      result.add(eleValue);
    } 
    return result;
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper) {
    if (value == null) {
      ByteBuffUtil.writeShort(out, (short)0);
      return;
    } 
    Collection<Object> collection = (Collection<Object>)value;
    int size = collection.size();
    ByteBuffUtil.writeShort(out, (short)size);
    for (Object elem : collection) {
      Class<?> clazz = elem.getClass();
      Serializer fieldCodec = Serializer.getSerializer(clazz);
      fieldCodec.encode(out, elem, wrapper);
    } 
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper, int type) {
    if (value == null) {
      if (type == 1) {
        ByteBuffUtil.writeByte(out, (byte)0);
      } else if (type != 2) {
        if (type == 3) {
          ByteBuffUtil.writeInt(out, 0);
        } else {
          ByteBuffUtil.writeShort(out, (short)0);
        } 
      } 
      return;
    } 
    Collection<Object> collection = (Collection<Object>)value;
    int size = collection.size();
    if (type == 1) {
      ByteBuffUtil.writeByte(out, (byte)size);
    } else if (type != 2) {
      if (type == 3) {
        ByteBuffUtil.writeInt(out, size);
      } else {
        ByteBuffUtil.writeShort(out, (short)size);
      } 
    } 
    for (Object elem : collection) {
      Class<?> clazz = elem.getClass();
      Serializer fieldCodec = Serializer.getSerializer(clazz);
      fieldCodec.encode(out, elem, wrapper);
    } 
  }
}
