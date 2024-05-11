
package com.kitty.cache;

import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import java.io.IOException;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import org.slf4j.Logger;

public class RedisSerializer
{
    private static final Logger log;
    public static final String CHARSET = "utf-8";
    
    public static <T extends Serializable> byte[] serialize(final T t) throws RuntimeException {
        if (t == null) {
            return null;
        }
        byte[] bytes = null;
        try {
            if (t.getClass() == Integer.TYPE || t.getClass() == Integer.class || t.getClass() == Long.TYPE || t.getClass() == Long.class || t.getClass() == Short.TYPE || t.getClass() == Short.class || t.getClass() == Double.TYPE || t.getClass() == Double.class || t.getClass() == Float.TYPE || t.getClass() == Float.class || t.getClass() == Byte.TYPE || t.getClass() == Byte.class || t.getClass() == Character.TYPE || t.getClass() == Character.class || t.getClass() == Boolean.TYPE || t.getClass() == Boolean.class || t.getClass() == String.class) {
                bytes = (t + "").getBytes("utf-8");
            }
            else {
                try {
                    bytes = JSONObject.toJSONString((Object)t).getBytes("utf-8");
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (final Exception e2) {
            e2.printStackTrace();
            RedisSerializer.log.error("序列化失败 " + e2.getMessage(), (Throwable)e2);
        }
        return bytes;
    }
    
    public static <T extends Serializable> T deserialize(final byte[] bytes, final Class<T> clazz) throws RuntimeException {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        try {
            if (clazz == Integer.TYPE || clazz == Integer.class || clazz == Long.TYPE || clazz == Long.class || clazz == Short.TYPE || clazz == Short.class || clazz == Double.TYPE || clazz == Double.class || clazz == Float.TYPE || clazz == Float.class || clazz == Byte.TYPE || clazz == Byte.class || clazz == Character.TYPE || clazz == Character.class || clazz == Boolean.TYPE || clazz == Boolean.class || clazz == String.class) {
                String s = null;
                s = new String(bytes, "utf-8");
                if (clazz == String.class) {
                    return (T)s;
                }
                if (clazz == Character.TYPE || clazz == Character.class) {
                    final Character c = s.charAt(0);
                    return (T)c;
                }
                final Method method = clazz.getMethod("valueOf", String.class);
                final Object object = method.invoke(null, s);
                return (T)object;
            }
            else {
                try {
                    return (T)JSONObject.parseObject(new String(bytes, "utf-8"), (Class)clazz);
                }
                catch (final Exception e) {
                    RedisSerializer.log.error("反序列化失败 " + e.getMessage(), (Throwable)e);
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    static {
        log = LoggerFactory.getLogger(RedisSerializer.class);
    }
}
