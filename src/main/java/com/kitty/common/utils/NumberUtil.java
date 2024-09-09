package com.kitty.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtil
{
    private static Pattern NUMBER_PATTERN;
    
    public NumberUtil() {
    }
    
    public static boolean booleanValue(Object object) {
        return booleaneValue(object, (boolean)Boolean.FALSE);
    }
    
    public static boolean booleaneValue(Object object, boolean defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Boolean.TYPE || object.getClass() == Boolean.class) {
            return (boolean)object;
        }
        try {
            return (boolean)Boolean.valueOf(object.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static byte byteValue(Object object) {
        return byteValue(object, (byte)Byte.valueOf("0"));
    }
    
    public static byte byteValue(Object object, byte defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Byte.TYPE || object.getClass() == Byte.class) {
            return (byte)object;
        }
        try {
            return (byte)Byte.valueOf(object.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static short shortValue(Object object) {
        return shortValue(object, (short)0);
    }
    
    public static short shortValue(Object object, short defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Short.TYPE || object.getClass() == Short.class) {
            return (short)object;
        }
        try {
            return (short)Short.valueOf(object.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static int intValue(Object object) {
        return intValue(object, 0);
    }
    
    public static int intValue(Object object, int defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Integer.TYPE || object.getClass() == Integer.class) {
            return (int)object;
        }
        try {
            return (int)Integer.valueOf(object.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static long longValue(Object object) {
        return longValue(object, 0L);
    }
    
    public static long longValue(Object object, long defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Long.TYPE || object.getClass() == Long.class) {
            return (long)object;
        }
        try {
            return (long)Long.valueOf(object.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static double doubleValue(Object object) {
        return doubleValue(object, 0.0);
    }
    
    public static double doubleValue(Object object, double defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Double.TYPE || object.getClass() == Double.class) {
            return (double)(long)object;
        }
        try {
            return (double)Double.valueOf(object.toString());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        str = str.replace(".", "");
        Matcher isNum = NumberUtil.NUMBER_PATTERN.matcher(str);
        return isNum.matches();
    }
    
    public static int median(int a, int b) {
        return (a & b) + ((a ^ b) >> 1);
    }
    
    static {
        NumberUtil.NUMBER_PATTERN = Pattern.compile("[0-9]+");
    }
}
