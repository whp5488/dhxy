
package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import org.apache.mina.core.buffer.IoBuffer;

public class BooleanSerializer extends Serializer
{
    @Override
    public Boolean decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        return ByteBuffUtil.readBoolean(in);
    }
    
    @Override
    public void encode(final IoBuffer out, final Object value, final Class<?> wrapper) {
        ByteBuffUtil.writeBoolean(out, (boolean)value);
    }
}
