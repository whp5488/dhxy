package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class ByteSerializer extends Serializer {
  public Byte decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    return Byte.valueOf(ByteBuffUtil.readByte(in));
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper) {
    if (value instanceof Integer) {
      ByteBuffUtil.writeByte(out, ((Integer)value).byteValue());
    } else if (value instanceof Short) {
      ByteBuffUtil.writeByte(out, ((Short)value).byteValue());
    } else if (value instanceof Long) {
      ByteBuffUtil.writeByte(out, ((Long)value).byteValue());
    } else {
      ByteBuffUtil.writeByte(out, ((Byte)value).byteValue());
    } 
  }
}
