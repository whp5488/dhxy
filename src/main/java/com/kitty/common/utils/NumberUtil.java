
package com.kitty.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtil
{
    private static Pattern NUMBER_PATTERN;
    
    public static boolean booleanValue(final Object object) {
        return booleaneValue(object, Boolean.FALSE);
    }
    
    public static boolean booleaneValue(final Object object, final boolean defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Boolean.TYPE || object.getClass() == Boolean.class) {
            return (boolean)object;
        }
        try {
            return Boolean.valueOf(object.toString());
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public static byte byteValue(final Object object) {
        return byteValue(object, Byte.valueOf("0"));
    }
    
    public static byte byteValue(final Object object, final byte defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Byte.TYPE || object.getClass() == Byte.class) {
            return (byte)object;
        }
        try {
            return Byte.valueOf(object.toString());
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public static short shortValue(final Object object) {
        return shortValue(object, (short)0);
    }
    
    public static short shortValue(final Object object, final short defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Short.TYPE || object.getClass() == Short.class) {
            return (short)object;
        }
        try {
            return Short.valueOf(object.toString());
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public static int intValue(final Object object) {
        return intValue(object, 0);
    }
    
    public static int intValue(final Object object, final int defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Integer.TYPE || object.getClass() == Integer.class) {
            return (int)object;
        }
        try {
            return Integer.valueOf(object.toString());
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public static long longValue(final Object object) {
        return longValue(object, 0L);
    }
    
    public static long longValue(final Object object, final long defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Long.TYPE || object.getClass() == Long.class) {
            return (long)object;
        }
        try {
            return Long.valueOf(object.toString());
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public static double doubleValue(final Object object) {
        return doubleValue(object, 0.0);
    }
    
    public static double doubleValue(final Object object, final double defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        if (object.getClass() == Double.TYPE || object.getClass() == Double.class) {
            return (double)(long)object;
        }
        try {
            return Double.valueOf(object.toString());
        }
        catch (final Exception e) {
            return defaultValue;
        }
    }
    
    public static boolean isNumber(String str) {
        if (str == null) {
            return false;
        }
        str = str.replace(".", "");
        final Matcher isNum = NumberUtil.NUMBER_PATTERN.matcher(str);
        return isNum.matches();
    }
    
    public static int median(final int a, final int b) {
        return (a & b) + ((a ^ b) >> 1);
    }
    
    static {
        NumberUtil.NUMBER_PATTERN = Pattern.compile("[0-9]+");
    }
}
