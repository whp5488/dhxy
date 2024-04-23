package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import com.kitty.common.utils.ReflectUtil;
import java.lang.reflect.Array;
import org.apache.mina.core.buffer.IoBuffer;

public class ArraySerializer extends Serializer {
  public Object decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    int size = ByteBuffUtil.readShort(in);
    Object array = ReflectUtil.newArray(type, wrapper, size);
    for (int i = 0; i < size; i++) {
      Serializer fieldCodec = getSerializer(wrapper);
      Object eleValue = fieldCodec.decode(in, wrapper, null);
      Array.set(array, i, eleValue);
    } 
    return array;
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper) {
    if (value == null) {
      ByteBuffUtil.writeShort(out, (short)0);
      return;
    } 
    int size = Array.getLength(value);
    ByteBuffUtil.writeShort(out, (short)size);
    encodeObject(out, value, wrapper, size);
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
    int size = Array.getLength(value);
    if (type == 1) {
      ByteBuffUtil.writeByte(out, (byte)size);
    } else if (type != 2) {
      if (type == 3) {
        ByteBuffUtil.writeInt(out, size);
      } else {
        ByteBuffUtil.writeShort(out, (short)size);
      } 
    } 
    encodeObject(out, value, wrapper, size);
  }
  
  private void encodeObject(IoBuffer out, Object value, Class<?> wrapper, int size) {
    for (int i = 0; i < size; i++) {
      Object elem = Array.get(value, i);
      Class<?> clazz = elem.getClass();
      Serializer fieldCodec = getSerializer(clazz);
      fieldCodec.encode(out, elem, wrapper);
    } 
  }
}
