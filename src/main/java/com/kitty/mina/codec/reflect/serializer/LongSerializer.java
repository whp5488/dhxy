package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class LongSerializer extends Serializer {
  public Long decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    return Long.valueOf(ByteBuffUtil.readLong(in));
  }
  
  public void encode(IoBuffer out, Object value, Class<?> wrapper) {
    ByteBuffUtil.writeLong(out, ((Long)value).longValue());
  }
  
  public void encodeUnsignedInt(IoBuffer out, Object value, Class<?> wrapper) {
    ByteBuffUtil.writeUnsignedInt(out, ((Long)value).longValue());
  }
}
