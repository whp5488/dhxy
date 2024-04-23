package com.kitty.cache;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisSerializer {
  private static final Logger log = LoggerFactory.getLogger(RedisSerializer.class);
  
  public static final String CHARSET = "utf-8";
  
  public static <T extends java.io.Serializable> byte[] serialize(T t) throws RuntimeException {
    if (t == null)
      return null; 
    byte[] bytes = null;
    try {
      if (t.getClass() == int.class || t.getClass() == Integer.class || t
        .getClass() == long.class || t.getClass() == Long.class || t
        .getClass() == short.class || t.getClass() == Short.class || t
        .getClass() == double.class || t.getClass() == Double.class || t
        .getClass() == float.class || t.getClass() == Float.class || t
        .getClass() == byte.class || t.getClass() == Byte.class || t
        .getClass() == char.class || t.getClass() == Character.class || t
        .getClass() == boolean.class || t.getClass() == Boolean.class || t
        .getClass() == String.class) {
        bytes = (new StringBuilder()).append(t).append("").toString().getBytes("utf-8");
      } else {
        try {
          bytes = JSONObject.toJSONString(t).getBytes("utf-8");
        } catch (IOException e) {
          e.printStackTrace();
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
      log.error("序列化失败 " + e.getMessage(), e);
    } 
    return bytes;
  }
  
  public static <T extends java.io.Serializable> T deserialize(byte[] bytes, Class<T> clazz) throws RuntimeException {
    if (bytes == null || bytes.length <= 0)
      return null; 
    try {
      if (clazz == int.class || clazz == Integer.class || clazz == long.class || clazz == Long.class || clazz == short.class || clazz == Short.class || clazz == double.class || clazz == Double.class || clazz == float.class || clazz == Float.class || clazz == byte.class || clazz == Byte.class || clazz == char.class || clazz == Character.class || clazz == boolean.class || clazz == Boolean.class || clazz == String.class) {
        String s = null;
        s = new String(bytes, "utf-8");
        if (clazz == String.class)
          return (T)s; 
        if (clazz == char.class || clazz == Character.class) {
          Character c = Character.valueOf(s.charAt(0));
          return (T)c;
        } 
        Method method = clazz.getMethod("valueOf", new Class[] { String.class });
        Object object = method.invoke(null, new Object[] { s });
        return (T)object;
      } 
      try {
        return (T)JSONObject.parseObject(new String(bytes, "utf-8"), clazz);
      } catch (Exception e) {
        log.error("反序列化失败 " + e.getMessage(), e);
      } finally {}
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return null;
  }
}
