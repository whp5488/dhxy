package com.kitty.mina.codec.reflect.serializer;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.mina.core.buffer.IoBuffer;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class Serializer
{
    private static Map<Class<?>, Serializer> class2Serializers;
    
    public Serializer() {
    }
    
    private static void register(Class<?> clazz, Serializer serializer) {
        Serializer.class2Serializers.put(clazz, serializer);
    }
    
    public static Serializer getSerializer(Class<?> clazz) {
        if (Serializer.class2Serializers.containsKey(clazz)) {
            return (Serializer)Serializer.class2Serializers.get(clazz);
        }
        if (clazz.isArray()) {
            return (Serializer)Serializer.class2Serializers.get(Object[].class);
        }
        Field[] fields = clazz.getDeclaredFields();
        LinkedHashMap<Field, Class<?>> sortedFields = new LinkedHashMap();
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
    
    public abstract Object decode(IoBuffer in, Class<?> type, Class<?> wrapper);
    
    public abstract void encode(IoBuffer out, Object value, Class<?> wrapper);
    
    static {
        Serializer.class2Serializers = new ConcurrentHashMap();
        register(Boolean.TYPE, new BooleanSerializer());
        register(Boolean.class, new BooleanSerializer());
        register(Byte.TYPE, new ByteSerializer());
        register(Byte.class, new ByteSerializer());
        register(Short.TYPE, new ShortSerializer());
        register(Short.class, new ShortSerializer());
        register(Integer.TYPE, new IntSerializer());
        register(Integer.class, new IntSerializer());
        register(Float.TYPE, new FloatSerializer());
        register(Float.class, new FloatSerializer());
        register(Double.TYPE, new DoubleSerializer());
        register(Double.class, new DoubleSerializer());
        register(Long.TYPE, new LongSerializer());
        register(Long.class, new LongSerializer());
        register(String.class, new StringSerializer());
        register(List.class, new CollectionSerializer());
        register(Set.class, new CollectionSerializer());
        register(Object[].class, new ArraySerializer());
    }
}
