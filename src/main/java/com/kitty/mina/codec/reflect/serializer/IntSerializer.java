package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class IntSerializer extends Serializer {
  public Integer decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    return Integer.valueOf(ByteBuffUtil.readInt(in));
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper) {
    if (value instanceof Short) {
      ByteBuffUtil.writeInt(out, ((Short)value).intValue());
    } else if (value instanceof Long) {
      ByteBuffUtil.writeInt(out, ((Long)value).intValue());
    } else if (value instanceof Byte) {
      ByteBuffUtil.writeInt(out, ((Byte)value).intValue());
    } else {
      ByteBuffUtil.writeInt(out, ((Integer)value).intValue());
    } 
  }
  
  public void encodeUnsignedShort(IoBuffer out, Object value, Class<?> wrapper) {
    if (value instanceof Short) {
      ByteBuffUtil.writeUnsignedShort(out, ((Short)value).intValue());
    } else if (value instanceof Long) {
      ByteBuffUtil.writeUnsignedShort(out, ((Long)value).intValue());
    } else if (value instanceof Byte) {
      ByteBuffUtil.writeUnsignedShort(out, ((Byte)value).intValue());
    } else {
      ByteBuffUtil.writeUnsignedShort(out, ((Integer)value).intValue());
    } 
  }
}
