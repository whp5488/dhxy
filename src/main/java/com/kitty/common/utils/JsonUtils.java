package com.kitty.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.ArrayType;
import org.codehaus.jackson.map.type.CollectionType;
import org.codehaus.jackson.map.type.MapType;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

public final class JsonUtils {
  private static TypeFactory typeFactory = TypeFactory.defaultInstance();
  
  private static final ObjectMapper MAPPER = (new ObjectMapper()).configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
  
  public static String object2String(Object object) {
    StringWriter writer = new StringWriter();
    try {
      MAPPER.writeValue(writer, object);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
    return writer.toString();
  }
  
  public static String object2String(Object object, TypeReference<?> ref) {
    StringWriter writer = new StringWriter();
    try {
      MAPPER.typedWriter(ref).writeValue(writer, object);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
    return writer.toString();
  }

  public static Object cloneObject(Object object) {
    String json = object2String(object);
    return string2Object(json, object.getClass());
  }
  
  public static <T> T string2Object(String json, Class<T> clazz) {
    JavaType type = typeFactory.constructType(clazz);
    try {
      return (T)MAPPER.readValue(json, type);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static String map2String(Map<?, ?> map) {
    StringWriter writer = new StringWriter();
    try {
      MAPPER.writeValue(writer, map);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
    return writer.toString();
  }
  
  public static Map<String, Object> string2Map(String json) {
    MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Object.class);
    try {
      return (Map<String, Object>)MAPPER.readValue(json, (JavaType)mapType);
    } catch (Exception e) {
      return null;
    } 
  }
  
  public static <K, V> Map<K, V> string2Map(String json, Class<K> keyClazz, Class<V> valueClazz) {
    MapType mapType = typeFactory.constructMapType(HashMap.class, keyClazz, valueClazz);
    try {
      return (Map<K, V>)MAPPER.readValue(json, (JavaType)mapType);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static <T> T[] string2Array(String json, Class<T> clazz) {
    ArrayType arrayType = ArrayType.construct(typeFactory.constructType(clazz));
    try {
      return (T[])MAPPER.readValue(json, (JavaType)arrayType);
    } catch (Exception e) {
      return null;
    } 
  }
  
  public static <C extends java.util.Collection<E>, E> C string2Collection(String json, Class<C> collectionType, Class<E> elemType) {
    CollectionType collectionType1 = typeFactory.constructCollectionType(collectionType, elemType);
    try {
      return (C)MAPPER.readValue(json, (JavaType)collectionType1);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static JSONObject objectToJson(Object object) {
    try {
      String json = (new Gson()).toJson(object);
      return (JSONObject)JSONObject.parse(json);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  public static <C extends java.util.Collection<E>, E> C string2Collection(String json, TypeReference valueTypeRef) {
    try {
      return (C)MAPPER.readValue(json, valueTypeRef);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
}
