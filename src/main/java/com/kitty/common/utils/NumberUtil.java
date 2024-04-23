package com.kitty.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NumberUtil {
  private static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+");
  
  public static boolean booleanValue(Object object) {
    return booleaneValue(object, Boolean.FALSE.booleanValue());
  }
  
  public static boolean booleaneValue(Object object, boolean defaultValue) {
    if (object == null)
      return defaultValue; 
    if (object.getClass() == boolean.class || object.getClass() == Boolean.class)
      return ((Boolean)object).booleanValue(); 
    try {
      return Boolean.valueOf(object.toString()).booleanValue();
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public static byte byteValue(Object object) {
    return byteValue(object, Byte.valueOf("0").byteValue());
  }
  
  public static byte byteValue(Object object, byte defaultValue) {
    if (object == null)
      return defaultValue; 
    if (object.getClass() == byte.class || object.getClass() == Byte.class)
      return ((Byte)object).byteValue(); 
    try {
      return Byte.valueOf(object.toString()).byteValue();
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public static short shortValue(Object object) {
    return shortValue(object, (short)0);
  }
  
  public static short shortValue(Object object, short defaultValue) {
    if (object == null)
      return defaultValue; 
    if (object.getClass() == short.class || object.getClass() == Short.class)
      return ((Short)object).shortValue(); 
    try {
      return Short.valueOf(object.toString()).shortValue();
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public static int intValue(Object object) {
    return intValue(object, 0);
  }
  
  public static int intValue(Object object, int defaultValue) {
    if (object == null)
      return defaultValue; 
    if (object.getClass() == int.class || object.getClass() == Integer.class)
      return ((Integer)object).intValue(); 
    try {
      return Integer.valueOf(object.toString()).intValue();
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public static long longValue(Object object) {
    return longValue(object, 0L);
  }
  
  public static long longValue(Object object, long defaultValue) {
    if (object == null)
      return defaultValue; 
    if (object.getClass() == long.class || object.getClass() == Long.class)
      return ((Long)object).longValue(); 
    try {
      return Long.valueOf(object.toString()).longValue();
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public static double doubleValue(Object object) {
    return doubleValue(object, 0.0D);
  }
  
  public static double doubleValue(Object object, double defaultValue) {
    if (object == null)
      return defaultValue; 
    if (object.getClass() == double.class || object.getClass() == Double.class)
      return ((Long)object).longValue(); 
    try {
      return Double.valueOf(object.toString()).doubleValue();
    } catch (Exception e) {
      return defaultValue;
    } 
  }
  
  public static boolean isNumber(String str) {
    if (str == null)
      return false; 
    str = str.replace(".", "");
    Matcher isNum = NUMBER_PATTERN.matcher(str);
    if (!isNum.matches())
      return false; 
    return true;
  }
  
  public static int median(int a, int b) {
    return (a & b) + ((a ^ b) >> 1);
  }
}
