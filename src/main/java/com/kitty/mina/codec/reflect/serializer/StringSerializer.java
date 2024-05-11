
package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class StringSerializer extends Serializer
{
    @Override
    public String decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        return ByteBuffUtil.readByteString(in);
    }
    
    public String decodeShort(final IoBuffer in) {
        return ByteBuffUtil.readShortString(in);
    }
    
    public void encodeShort(final IoBuffer out, final String msg) {
        ByteBuffUtil.writeShortString(out, msg);
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        ByteBuffUtil.writeByteString(out, (String)value);
    }
}
