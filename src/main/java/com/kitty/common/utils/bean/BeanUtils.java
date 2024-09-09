package com.kitty.common.utils.bean;

import java.util.regex.Matcher;
import java.util.ArrayList;
import java.lang.reflect.Method;
import java.util.List;
import java.util.regex.Pattern;

public class BeanUtils extends org.springframework.beans.BeanUtils
{
    private static final int BEAN_METHOD_PROP_INDEX = 3;
    private static final Pattern GET_PATTERN;
    private static final Pattern SET_PATTERN;
    
    public BeanUtils() {
    }
    
    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static List<Method> getSetterMethods(Object obj) {
        List<Method> setterMethods = new ArrayList<>();
        Method[] methods;
        for (Method method : methods = obj.getClass().getMethods()) {
            Matcher m = BeanUtils.SET_PATTERN.matcher(method.getName());
            if (m.matches() && method.getParameterTypes().length == 1) {
                setterMethods.add(method);
            }
        }
        return setterMethods;
    }
    
    public static List<Method> getGetterMethods(Object obj) {
        List<Method> getterMethods = new ArrayList<>();
        Method[] methods;
        for (Method method : methods = obj.getClass().getMethods()) {
            Matcher m = BeanUtils.GET_PATTERN.matcher(method.getName());
            if (m.matches() && method.getParameterTypes().length == 0) {
                getterMethods.add(method);
            }
        }
        return getterMethods;
    }
    
    public static boolean isMethodPropEquals(String m1, String m2) {
        return m1.substring(3).equals(m2.substring(3));
    }
    
    static {
        GET_PATTERN = Pattern.compile("get(\\p{javaUpperCase}\\w*)");
        SET_PATTERN = Pattern.compile("set(\\p{javaUpperCase}\\w*)");
    }
}
