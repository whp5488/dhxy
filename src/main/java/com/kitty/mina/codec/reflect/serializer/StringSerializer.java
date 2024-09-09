package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class StringSerializer extends Serializer
{
    public StringSerializer() {
    }
    
    @Override
    public String decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
        return ByteBuffUtil.readByteString(in);
    }
    
    public String decodeShort(IoBuffer in) {
        return ByteBuffUtil.readShortString(in);
    }
    
    public void encodeShort(IoBuffer out, String msg) {
        ByteBuffUtil.writeShortString(out, msg);
    }
    
    @Override
    public void encode(IoBuffer out, Object value, Class<?> wrapper) {
        ByteBuffUtil.writeByteString(out, (String)value);
    }
}
