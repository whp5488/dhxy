package com.kitty.mina.codec.reflect.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.buffer.IoBuffer;

public abstract class Serializer {
  private static Map<Class<?>, Serializer> class2Serializers = new ConcurrentHashMap<>();
  
  static {
    register(boolean.class, new BooleanSerializer());
    register(Boolean.class, new BooleanSerializer());
    register(byte.class, new ByteSerializer());
    register(Byte.class, new ByteSerializer());
    register(short.class, new ShortSerializer());
    register(Short.class, new ShortSerializer());
    register(int.class, new IntSerializer());
    register(Integer.class, new IntSerializer());
    register(float.class, new FloatSerializer());
    register(Float.class, new FloatSerializer());
    register(double.class, new DoubleSerializer());
    register(Double.class, new DoubleSerializer());
    register(long.class, new LongSerializer());
    register(Long.class, new LongSerializer());
    register(String.class, new StringSerializer());
    register(List.class, new CollectionSerializer());
    register(Set.class, new CollectionSerializer());
    register(Object[].class, new ArraySerializer());
  }
  
  private static void register(Class<?> clazz, Serializer serializer) {
    class2Serializers.put(clazz, serializer);
  }
  
  public static Serializer getSerializer(Class<?> clazz) {
    if (class2Serializers.containsKey(clazz))
      return class2Serializers.get(clazz); 
    if (clazz.isArray())
      return class2Serializers.get(Object[].class); 
    Field[] fields = clazz.getDeclaredFields();
    LinkedHashMap<Field, Class<?>> sortedFields = new LinkedHashMap<>();
    List<FieldCodecMeta> fieldsMeta = new ArrayList<>();
    for (Field field : fields) {
      int modifier = field.getModifiers();
      if (!Modifier.isFinal(modifier) && !Modifier.isStatic(modifier) && !Modifier.isTransient(modifier)) {
        field.setAccessible(true);
        sortedFields.put(field, field.getType());
        Class<?> type = field.getType();
        Serializer serializer = getSerializer(type);
        fieldsMeta.add(FieldCodecMeta.valueOf(field, serializer));
      } 
    } 
    Serializer messageCodec = MessageSerializer.valueOf(fieldsMeta);
    register(clazz, messageCodec);
    return messageCodec;
  }
  
  public abstract Object decode(IoBuffer paramIoBuffer, Class<?> paramClass1, Class<?> paramClass2);
  
  public abstract void encode(IoBuffer paramIoBuffer, Object paramObject, Class<?> paramClass);
}
