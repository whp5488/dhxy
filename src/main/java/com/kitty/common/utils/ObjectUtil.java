
package com.kitty.common.utils;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ObjectUtil
{
    public static Object getFieldValueByName(String fieldName, final Object o) {
        try {
            fieldName = fieldName.toLowerCase();
            final String firstLetter = fieldName.substring(0, 1).toUpperCase();
            final String getter = "get" + firstLetter + fieldName.substring(1);
            final Method method = o.getClass().getMethod(getter);
            final Object value = method.invoke(o);
            return value;
        }
        catch (final Exception e) {
            return null;
        }
    }
    
    public static String[] getFiledName(final Object o) {
        final Field[] fields = o.getClass().getDeclaredFields();
        final String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; ++i) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }
    
    public static List getFiledsInfo(final Object o) {
        final Field[] fields = o.getClass().getDeclaredFields();
        final String[] fieldNames = new String[fields.length];
        final List list = new ArrayList();
        Map infoMap = null;
        for (int i = 0; i < fields.length; ++i) {
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }
    
    public Object[] getFiledValues(final Object o) {
        final String[] fieldNames = getFiledName(o);
        final Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; ++i) {
            value[i] = getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }
}
