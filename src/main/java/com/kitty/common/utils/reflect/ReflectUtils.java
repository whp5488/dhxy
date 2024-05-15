
package com.kitty.common.utils.reflect;

import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.Validate;
import org.apache.poi.ss.usermodel.DateUtil;
import com.kitty.common.utils.DateUtils;
import java.util.Date;
import com.kitty.common.utils.text.Convert;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

public class ReflectUtils
{
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String CGLIB_CLASS_SEPARATOR = "$$";
    private static Logger logger;
    
    public static <E> E invokeGetter(final Object obj, final String propertyName) {
        Object object = obj;
        for (final String name : StringUtils.split(propertyName, ".")) {
            final String getterMethodName = "get" + StringUtils.capitalize(name);
            object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
        }
        return (E)object;
    }
    
    public static <E> void invokeSetter(final Object obj, final String propertyName, final E value) {
        Object object = obj;
        final String[] names = StringUtils.split(propertyName, ".");
        for (int i = 0; i < names.length; ++i) {
            if (i < names.length - 1) {
                final String getterMethodName = "get" + StringUtils.capitalize(names[i]);
                object = invokeMethod(object, getterMethodName, new Class[0], new Object[0]);
            }
            else {
                final String setterMethodName = "set" + StringUtils.capitalize(names[i]);
                invokeMethodByName(object, setterMethodName, new Object[] { value });
            }
        }
    }
    
    public static <E> E getFieldValue(final Object obj, final String fieldName) {
        final Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            ReflectUtils.logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            return null;
        }
        E result = null;
        try {
            result = (E)field.get(obj);
        }
        catch (final IllegalAccessException e) {
            ReflectUtils.logger.error("不可能抛出的异常{}", e.getMessage());
        }
        return result;
    }
    
    public static <E> void setFieldValue(final Object obj, final String fieldName, final E value) {
        final Field field = getAccessibleField(obj, fieldName);
        if (field == null) {
            ReflectUtils.logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + fieldName + "] 字段 ");
            return;
        }
        try {
            field.set(obj, value);
        }
        catch (final IllegalAccessException e) {
            ReflectUtils.logger.error("不可能抛出的异常: {}", e.getMessage());
        }
    }
    
    public static <E> E invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes, final Object[] args) {
        if (obj == null || methodName == null) {
            return null;
        }
        final Method method = getAccessibleMethod(obj, methodName, parameterTypes);
        if (method == null) {
            ReflectUtils.logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
            return null;
        }
        try {
            return (E)method.invoke(obj, args);
        }
        catch (final Exception e) {
            final String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }
    
    public static <E> E invokeMethodByName(final Object obj, final String methodName, final Object[] args) {
        final Method method = getAccessibleMethodByName(obj, methodName, args.length);
        if (method == null) {
            ReflectUtils.logger.debug("在 [" + obj.getClass() + "] 中，没有找到 [" + methodName + "] 方法 ");
            return null;
        }
        try {
            final Class<?>[] cs = method.getParameterTypes();
            for (int i = 0; i < cs.length; ++i) {
                if (args[i] != null && !args[i].getClass().equals(cs[i])) {
                    if (cs[i] == String.class) {
                        args[i] = Convert.toStr(args[i]);
                        if (StringUtils.endsWith((CharSequence)args[i], ".0")) {
                            args[i] = StringUtils.substringBefore((String)args[i], ".0");
                        }
                    }
                    else if (cs[i] == Integer.class) {
                        args[i] = Convert.toInt(args[i]);
                    }
                    else if (cs[i] == Long.class) {
                        args[i] = Convert.toLong(args[i]);
                    }
                    else if (cs[i] == Double.class) {
                        args[i] = Convert.toDouble(args[i]);
                    }
                    else if (cs[i] == Float.class) {
                        args[i] = Convert.toFloat(args[i]);
                    }
                    else if (cs[i] == Date.class) {
                        if (args[i] instanceof String) {
                            args[i] = DateUtils.parseDate(args[i]);
                        }
                        else {
                            args[i] = DateUtil.getJavaDate((double)args[i]);
                        }
                    }
                    else if (cs[i] == Boolean.TYPE || cs[i] == Boolean.class) {
                        args[i] = Convert.toBool(args[i]);
                    }
                }
            }
            return (E)method.invoke(obj, args);
        }
        catch (final Exception e) {
            final String msg = "method: " + method + ", obj: " + obj + ", args: " + args + "";
            throw convertReflectionExceptionToUnchecked(msg, e);
        }
    }
    
    public static Field getAccessibleField(final Object obj, final String fieldName) {
        if (obj == null) {
            return null;
        }
        Validate.notBlank((CharSequence)fieldName, "fieldName can't be blank");
        Class<?> superClass = obj.getClass();
        while (superClass != Object.class) {
            try {
                final Field field = superClass.getDeclaredField(fieldName);
                makeAccessible(field);
                return field;
            }
            catch (final NoSuchFieldException e) {
                superClass = superClass.getSuperclass();
                continue;
            }
        }
        return null;
    }
    
    public static Method getAccessibleMethod(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        if (obj == null) {
            return null;
        }
        Validate.notBlank((CharSequence)methodName, "methodName can't be blank");
        Class<?> searchType = obj.getClass();
        while (searchType != Object.class) {
            try {
                final Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            }
            catch (final NoSuchMethodException e) {
                searchType = searchType.getSuperclass();
                continue;
            }
        }
        return null;
    }
    
    public static Method getAccessibleMethodByName(final Object obj, final String methodName, final int argsNum) {
        if (obj == null) {
            return null;
        }
        Validate.notBlank((CharSequence)methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            final Method[] declaredMethods;
            final Method[] methods = declaredMethods = searchType.getDeclaredMethods();
            for (final Method method : declaredMethods) {
                if (method.getName().equals(methodName) && method.getParameterTypes().length == argsNum) {
                    makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }
    
    public static void makeAccessible(final Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }
    
    public static void makeAccessible(final Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers()) || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }
    
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }
    
    public static Class getClassGenricType(final Class clazz, final int index) {
        final Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            ReflectUtils.logger.debug(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }
        final Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            ReflectUtils.logger.debug("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            ReflectUtils.logger.debug(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }
        return (Class)params[index];
    }
    
    public static Class<?> getUserClass(final Object instance) {
        if (instance == null) {
            throw new RuntimeException("Instance must not be null");
        }
        final Class clazz = instance.getClass();
        if (clazz != null && clazz.getName().contains("$$")) {
            final Class<?> superClass = clazz.getSuperclass();
            if (superClass != null && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }
    
    public static RuntimeException convertReflectionExceptionToUnchecked(final String msg, final Exception e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(msg, e);
        }
        if (e instanceof InvocationTargetException) {
            return new RuntimeException(msg, ((InvocationTargetException)e).getTargetException());
        }
        return new RuntimeException(msg, e);
    }
    
    static {
        ReflectUtils.logger = LoggerFactory.getLogger(ReflectUtils.class);
    }
}
