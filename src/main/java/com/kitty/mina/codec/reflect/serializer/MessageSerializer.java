
package com.kitty.mina.codec.reflect.serializer;

import org.nutz.json.Json;
import org.nutz.lang.Lang;
import com.kitty.mina.annotation.ShortField;
import com.kitty.mina.annotation.IntegerField;
import com.kitty.mina.annotation.LongField;
import java.lang.reflect.Field;
import java.util.Iterator;
import com.kitty.mina.annotation.ListField;
import com.kitty.common.utils.ByteBuffUtil;
import com.kitty.mina.annotation.StringField;
import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.slf4j.Logger;

public class MessageSerializer extends Serializer
{
    private Logger logger;
    private List<FieldCodecMeta> fieldsMeta;
    
    public MessageSerializer() {
        this.logger = LoggerFactory.getLogger(MessageSerializer.class);
    }
    
    public static MessageSerializer valueOf(final List<FieldCodecMeta> fieldsMeta) {
        final MessageSerializer serializer = new MessageSerializer();
        serializer.fieldsMeta = fieldsMeta;
        return serializer;
    }
    
    @Override
    public Object decode(final IoBuffer in, final Class<?> type, final Class<?> wrapper) {
        try {
            final Object bean = type.newInstance();
            for (final FieldCodecMeta fieldMeta : this.fieldsMeta) {
                final Field field = fieldMeta.getField();
                final Serializer fieldCodec = fieldMeta.getSerializer();
                Object value = null;
                if (fieldCodec instanceof StringSerializer) {
                    final StringField stringField = field.getAnnotation(StringField.class);
                    final StringSerializer stringSerializer = (StringSerializer)fieldCodec;
                    if (stringField != null) {
                        if (stringField.value() == 1) {
                            value = stringSerializer.decodeShort(in);
                        }
                        else if (stringField.value() == 100) {
                            value = ByteBuffUtil.readFullString(in);
                        }
                        else {
                            value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
                        }
                    }
                    else {
                        value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
                    }
                }
                else if (fieldCodec instanceof CollectionSerializer) {
                    final ListField listField = field.getAnnotation(ListField.class);
                    final CollectionSerializer collectionSerializer = (CollectionSerializer)fieldCodec;
                    if (listField != null) {
                        if (listField.value() == 1) {
                            value = collectionSerializer.decodeByte(in, fieldMeta.getType(), fieldMeta.getWrapper());
                        }
                        else {
                            value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
                        }
                    }
                    else {
                        value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
                    }
                }
                else {
                    value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
                }
                field.set(bean, value);
            }
            return bean;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public void encode(final IoBuffer out, final Object message, final Class<?> wrapper) {
        if (message == null) {
            return;
        }
        Object value = null;
        try {
            byte valueType = -1;
            for (final FieldCodecMeta fieldMeta : this.fieldsMeta) {
                final Field field = fieldMeta.getField();
                Serializer fieldCodec = fieldMeta.getSerializer();
                value = field.get(message);
                if (valueType == 1 || valueType == 6) {
                    fieldCodec = Serializer.getSerializer(Byte.class);
                }
                else if (valueType == 2 || valueType == 7 || valueType == 85) {
                    fieldCodec = Serializer.getSerializer(Short.class);
                }
                else if (valueType == 4) {
                    fieldCodec = Serializer.getSerializer(String.class);
                }
                else if (valueType == 3) {
                    fieldCodec = Serializer.getSerializer(Integer.class);
                }
                if (fieldCodec instanceof StringSerializer) {
                    final StringField stringField = field.getAnnotation(StringField.class);
                    final StringSerializer stringSerializer = (StringSerializer)fieldCodec;
                    if (stringField != null) {
                        if (stringField.value() == 1) {
                            stringSerializer.encodeShort(out, (String)value);
                        }
                        else if (stringField.value() == 100) {
                            ByteBuffUtil.writeFullString(out, (String)value);
                        }
                        else {
                            fieldCodec.encode(out, value, fieldMeta.getWrapper());
                        }
                    }
                    else {
                        fieldCodec.encode(out, value, fieldMeta.getWrapper());
                    }
                }
                else if (fieldCodec instanceof CollectionSerializer) {
                    final ListField listField = field.getAnnotation(ListField.class);
                    final CollectionSerializer collectionSerializer = (CollectionSerializer)fieldCodec;
                    if (listField != null) {
                        collectionSerializer.encode(out, value, fieldMeta.getWrapper(), listField.value());
                    }
                    else {
                        fieldCodec.encode(out, value, fieldMeta.getWrapper());
                    }
                }
                else if (fieldCodec instanceof ArraySerializer) {
                    final ListField listField = field.getAnnotation(ListField.class);
                    final ArraySerializer arraySerializer = (ArraySerializer)fieldCodec;
                    if (listField != null) {
                        arraySerializer.encode(out, value, fieldMeta.getWrapper(), listField.value());
                    }
                    else {
                        fieldCodec.encode(out, value, fieldMeta.getWrapper());
                    }
                }
                else if (fieldCodec instanceof LongSerializer) {
                    final LongField longField = field.getAnnotation(LongField.class);
                    if (longField != null && longField.value() == 1) {
                        final LongSerializer longSerializer = (LongSerializer)fieldCodec;
                        longSerializer.encodeUnsignedInt(out, value, fieldMeta.getWrapper());
                    }
                    else {
                        fieldCodec.encode(out, value, fieldMeta.getWrapper());
                    }
                }
                else if (fieldCodec instanceof IntSerializer) {
                    final IntegerField integerField = field.getAnnotation(IntegerField.class);
                    if (integerField != null && integerField.value() == 1) {
                        final IntSerializer intSerializer = (IntSerializer)fieldCodec;
                        intSerializer.encodeUnsignedShort(out, value, fieldMeta.getWrapper());
                    }
                    else {
                        fieldCodec.encode(out, value, fieldMeta.getWrapper());
                    }
                }
                else if (fieldCodec instanceof ShortSerializer) {
                    final ShortField shortField = field.getAnnotation(ShortField.class);
                    if (shortField != null && shortField.value() == 1) {
                        final ShortSerializer shortSerializer = (ShortSerializer)fieldCodec;
                        shortSerializer.encodeUnsignedByte(out, value, fieldMeta.getWrapper());
                    }
                    else {
                        fieldCodec.encode(out, value, fieldMeta.getWrapper());
                    }
                }
                else {
                    fieldCodec.encode(out, value, fieldMeta.getWrapper());
                }
                if ("VT".equals(field.getName())) {
                    valueType = (byte)value;
                }
            }
        }
        catch (final Exception e) {
            this.logger.error("{}{}{}", new Object[] { Lang.getStackTrace((Throwable)e), Json.toJson(message), message.getClass().getName() });
            e.printStackTrace();
        }
    }
}
