package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class DoubleSerializer extends Serializer {
  public Double decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    return Double.valueOf(ByteBuffUtil.readDouble(in));
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper) {
    ByteBuffUtil.writeDouble(out, ((Double)value).doubleValue());
  }
}
