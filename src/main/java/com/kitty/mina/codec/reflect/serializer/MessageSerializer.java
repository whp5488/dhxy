package com.kitty.mina.codec.reflect.serializer;

import com.kitty.common.utils.ByteBuffUtil;
import com.kitty.mina.annotation.IntegerField;
import com.kitty.mina.annotation.ListField;
import com.kitty.mina.annotation.LongField;
import com.kitty.mina.annotation.ShortField;
import com.kitty.mina.annotation.StringField;
import java.lang.reflect.Field;
import java.util.List;
import org.apache.mina.core.buffer.IoBuffer;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageSerializer extends Serializer {
  private Logger logger = LoggerFactory.getLogger(MessageSerializer.class);
  
  private List<FieldCodecMeta> fieldsMeta;
  
  public static MessageSerializer valueOf(List<FieldCodecMeta> fieldsMeta) {
    MessageSerializer serializer = new MessageSerializer();
    serializer.fieldsMeta = fieldsMeta;
    return serializer;
  }
  
  public Object decode(IoBuffer in, Class<?> type, Class<?> wrapper) {
    try {
      Object bean = type.newInstance();
      for (FieldCodecMeta fieldMeta : this.fieldsMeta) {
        Field field = fieldMeta.getField();
        Serializer fieldCodec = fieldMeta.getSerializer();
        Object value = null;
        if (fieldCodec instanceof StringSerializer) {
          StringField stringField = field.<StringField>getAnnotation(StringField.class);
          StringSerializer stringSerializer = (StringSerializer)fieldCodec;
          if (stringField != null) {
            if (stringField.value() == 1) {
              value = stringSerializer.decodeShort(in);
            } else if (stringField.value() == 100) {
              value = ByteBuffUtil.readFullString(in);
            } else {
              value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
            } 
          } else {
            value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
          } 
        } else if (fieldCodec instanceof CollectionSerializer) {
          ListField listField = field.<ListField>getAnnotation(ListField.class);
          CollectionSerializer collectionSerializer = (CollectionSerializer)fieldCodec;
          if (listField != null) {
            if (listField.value() == 1) {
              value = collectionSerializer.decodeByte(in, fieldMeta.getType(), fieldMeta.getWrapper());
            } else {
              value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
            } 
          } else {
            value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
          } 
        } else {
          value = fieldCodec.decode(in, fieldMeta.getType(), fieldMeta.getWrapper());
        } 
        field.set(bean, value);
      } 
      return bean;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public void encode(IoBuffer out, Object message, Class<?> wrapper) {
    if (message == null)
      return; 
    Object value = null;
    try {
      byte valueType = -1;
      for (FieldCodecMeta fieldMeta : this.fieldsMeta) {
        Field field = fieldMeta.getField();
        Serializer fieldCodec = fieldMeta.getSerializer();
        value = field.get(message);
        if (valueType == 1 || valueType == 6) {
          fieldCodec = Serializer.getSerializer(Byte.class);
        } else if (valueType == 2 || valueType == 7 || valueType == 85) {
          fieldCodec = Serializer.getSerializer(Short.class);
        } else if (valueType == 4) {
          fieldCodec = Serializer.getSerializer(String.class);
        } else if (valueType == 3) {
          fieldCodec = Serializer.getSerializer(Integer.class);
        } 
        if (fieldCodec instanceof StringSerializer) {
          StringField stringField = field.<StringField>getAnnotation(StringField.class);
          StringSerializer stringSerializer = (StringSerializer)fieldCodec;
          if (stringField != null) {
            if (stringField.value() == 1) {
              stringSerializer.encodeShort(out, (String)value);
            } else if (stringField.value() == 100) {
              ByteBuffUtil.writeFullString(out, (String)value);
            } else {
              fieldCodec.encode(out, value, fieldMeta.getWrapper());
            } 
          } else {
            fieldCodec.encode(out, value, fieldMeta.getWrapper());
          } 
        } else if (fieldCodec instanceof CollectionSerializer) {
          ListField listField = field.<ListField>getAnnotation(ListField.class);
          CollectionSerializer collectionSerializer = (CollectionSerializer)fieldCodec;
          if (listField != null) {
            collectionSerializer.encode(out, value, fieldMeta.getWrapper(), listField.value());
          } else {
            fieldCodec.encode(out, value, fieldMeta.getWrapper());
          } 
        } else if (fieldCodec instanceof ArraySerializer) {
          ListField listField = field.<ListField>getAnnotation(ListField.class);
          ArraySerializer arraySerializer = (ArraySerializer)fieldCodec;
          if (listField != null) {
            arraySerializer.encode(out, value, fieldMeta.getWrapper(), listField.value());
          } else {
            fieldCodec.encode(out, value, fieldMeta.getWrapper());
          } 
        } else if (fieldCodec instanceof LongSerializer) {
          LongField longField = field.<LongField>getAnnotation(LongField.class);
          if (longField != null && longField.value() == 1) {
            LongSerializer longSerializer = (LongSerializer)fieldCodec;
            longSerializer.encodeUnsignedInt(out, value, fieldMeta.getWrapper());
          } else {
            fieldCodec.encode(out, value, fieldMeta.getWrapper());
          } 
        } else if (fieldCodec instanceof IntSerializer) {
          IntegerField integerField = field.<IntegerField>getAnnotation(IntegerField.class);
          if (integerField != null && integerField.value() == 1) {
            IntSerializer intSerializer = (IntSerializer)fieldCodec;
            intSerializer.encodeUnsignedShort(out, value, fieldMeta.getWrapper());
          } else {
            fieldCodec.encode(out, value, fieldMeta.getWrapper());
          } 
        } else if (fieldCodec instanceof ShortSerializer) {
          ShortField shortField = field.<ShortField>getAnnotation(ShortField.class);
          if (shortField != null && shortField.value() == 1) {
            ShortSerializer shortSerializer = (ShortSerializer)fieldCodec;
            shortSerializer.encodeUnsignedByte(out, value, fieldMeta.getWrapper());
          } else {
            fieldCodec.encode(out, value, fieldMeta.getWrapper());
          } 
        } else {
          fieldCodec.encode(out, value, fieldMeta.getWrapper());
        } 
        if ("VT".equals(field.getName()))
          valueType = ((Byte)value).byteValue(); 
      } 
    } catch (Exception e) {
      this.logger.error("{}{}{}", new Object[] { Lang.getStackTrace(e), Json.toJson(message), message.getClass().getName() });
      e.printStackTrace();
    } 
  }
}
