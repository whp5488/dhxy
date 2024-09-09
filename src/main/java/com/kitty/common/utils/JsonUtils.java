package com.kitty.common.utils;

import org.codehaus.jackson.JsonParser;
import com.google.gson.Gson;
import com.alibaba.fastjson.JSONObject;
import java.util.Collection;
import org.codehaus.jackson.map.type.ArrayType;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;
import java.io.StringWriter;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

public final class JsonUtils
{
    private static TypeFactory typeFactory;
    private static final ObjectMapper MAPPER;
    
    public JsonUtils() {
    }
    
    public static String object2String(Object object) {
        StringWriter writer = new StringWriter();
        try {
            JsonUtils.MAPPER.writeValue(writer, object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
    
    public static String object2String(Object object, TypeReference<?> ref) {
        StringWriter writer = new StringWriter();
        try {
            JsonUtils.MAPPER.typedWriter(ref).writeValue(writer, object);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
    
    public static <T> T cloneObject(Object object) {
        String json = object2String(object);
        return (T)string2Object(json, object.getClass());
    }
    
    public static <T> T string2Object(String json, Class<T> clazz) {
        JavaType type = JsonUtils.typeFactory.constructType(clazz);
        try {
            return JsonUtils.MAPPER.readValue(json, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static String map2String(Map<?, ?> map) {
        StringWriter writer = new StringWriter();
        try {
            JsonUtils.MAPPER.writeValue(writer, map);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
    
    public static Map<String, Object> string2Map(String json) {
        JavaType type = JsonUtils.typeFactory.constructMapType(HashMap.class, String.class, Object.class);
        try {
            return (Map)JsonUtils.MAPPER.readValue(json, type);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static <K, V> Map<K, V> string2Map(String json, Class<K> keyClazz, Class<V> valueClazz) {
        JavaType type = JsonUtils.typeFactory.constructMapType(HashMap.class, keyClazz, valueClazz);
        try {
            return (Map)JsonUtils.MAPPER.readValue(json, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T[] string2Array(String json, Class<T> clazz) {
        JavaType type = ArrayType.construct(JsonUtils.typeFactory.constructType(clazz));
        try {
            return (T[])(Object[])JsonUtils.MAPPER.readValue(json, type);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public static <C extends Collection<E>, E> C string2Collection(String json, Class<C> collectionType, Class<E> elemType) {
        JavaType type = JsonUtils.typeFactory.constructCollectionType(collectionType, elemType);
        try {
            return (C)JsonUtils.MAPPER.readValue(json, type);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static JSONObject objectToJson(Object object) {
        try {
            String json = new Gson().toJson(object);
            return (JSONObject)JSONObject.parse(json);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <C extends Collection<E>, E> C string2Collection(String json, TypeReference valueTypeRef) {
        try {
            return (C)JsonUtils.MAPPER.readValue(json, valueTypeRef);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    static {
        JsonUtils.typeFactory = TypeFactory.defaultInstance();
        MAPPER = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
    }
}
