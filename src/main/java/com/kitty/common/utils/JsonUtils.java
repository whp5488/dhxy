
package com.kitty.common.utils;

import org.codehaus.jackson.JsonParser;
import com.google.gson.Gson;
import com.alibaba.fastjson.JSONObject;
import java.util.Collection;
import org.codehaus.jackson.map.type.ArrayType;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;
import java.lang.reflect.Type;
import org.codehaus.jackson.type.TypeReference;
import java.io.Writer;
import java.io.StringWriter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

public final class JsonUtils
{
    private static TypeFactory typeFactory;
    private static final ObjectMapper MAPPER;
    
    public static String object2String(final Object object) {
        final StringWriter writer = new StringWriter();
        try {
            JsonUtils.MAPPER.writeValue((Writer)writer, object);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
    
    public static String object2String(final Object object, final TypeReference<?> ref) {
        final StringWriter writer = new StringWriter();
        try {
            JsonUtils.MAPPER.typedWriter((TypeReference)ref).writeValue((Writer)writer, object);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
    
    public static <T> T cloneObject(final Object object) {
        final String json = object2String(object);
        return (T) string2Object(json, object.getClass());
    }
    
    public static <T> T string2Object(final String json, final Class<T> clazz) {
        final JavaType type = JsonUtils.typeFactory.constructType((Type)clazz);
        try {
            return (T)JsonUtils.MAPPER.readValue(json, type);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String map2String(final Map<?, ?> map) {
        final StringWriter writer = new StringWriter();
        try {
            JsonUtils.MAPPER.writeValue((Writer)writer, (Object)map);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
    
    public static Map<String, Object> string2Map(final String json) {
        final JavaType type = (JavaType)JsonUtils.typeFactory.constructMapType(HashMap.class, (Class)String.class, (Class)Object.class);
        try {
            return (Map)JsonUtils.MAPPER.readValue(json, type);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static <K, V> Map<K, V> string2Map(final String json, final Class<K> keyClazz, final Class<V> valueClazz) {
        final JavaType type = JsonUtils.typeFactory.constructMapType(HashMap.class, keyClazz, valueClazz);
        try {
            return JsonUtils.MAPPER.readValue(json, type);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T[] string2Array(final String json, final Class<T> clazz) {
        final JavaType type = ArrayType.construct(JsonUtils.typeFactory.constructType(clazz));
        try {
            return JsonUtils.MAPPER.readValue(json, type);
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static <C extends Collection<E>, E> C string2Collection(final String json, final Class<C> collectionType, final Class<E> elemType) {
        final JavaType type = JsonUtils.typeFactory.constructCollectionType(collectionType, elemType);
        try {
            return (C)JsonUtils.MAPPER.readValue(json, type);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject objectToJson(final Object object) {
        try {
            final String json = new Gson().toJson(object);
            return (JSONObject)JSONObject.parse(json);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <C extends Collection<E>, E> C string2Collection(final String json, final TypeReference valueTypeRef) {
        try {
            return (C)JsonUtils.MAPPER.readValue(json, valueTypeRef);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        JsonUtils.typeFactory = TypeFactory.defaultInstance();
        MAPPER = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
}
