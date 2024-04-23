package com.kitty.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class BigDecimalUtil {
  public static String addition(String one, String two) {
    BigDecimal decimal_one = new BigDecimal(one);
    BigDecimal decimal_two = new BigDecimal(two);
    return decimal_one.add(decimal_two).stripTrailingZeros().toPlainString();
  }
  
  public static double additionDouble(double one, double two) {
    String resouce = addition(String.valueOf(one), String.valueOf(two));
    return Double.valueOf(resouce).doubleValue();
  }
  
  public static String subtract(String one, String two) {
    BigDecimal decimal_one = new BigDecimal(one);
    BigDecimal decimal_two = new BigDecimal(two);
    return decimal_one.subtract(decimal_two).stripTrailingZeros().toPlainString();
  }
  
  public static String multiply(String one, String two) {
    BigDecimal decimal_one = new BigDecimal(one);
    BigDecimal decimal_two = new BigDecimal(two);
    return decimal_one.multiply(decimal_two).stripTrailingZeros().toPlainString();
  }
  
  public static String divide(String one, String two) {
    String decimal = "0";
    if (decimal.equals(two))
      return decimal; 
    BigDecimal decimal_one = new BigDecimal(one);
    BigDecimal decimal_two = new BigDecimal(two);
    return decimal_one.divide(decimal_two, 2, 4).stripTrailingZeros().toPlainString();
  }
  
  public static String divide(String one, String two, int scale) {
    String decimal = "0";
    if (decimal.equals(two))
      return decimal; 
    BigDecimal decimal_one = new BigDecimal(one);
    BigDecimal decimal_two = new BigDecimal(two);
    return decimal_one.divide(decimal_two, scale, 4).stripTrailingZeros().toPlainString();
  }
  
  public static String whole(String one) {
    int floor = (int)Math.floor(Double.parseDouble(one));
    return String.valueOf(floor);
  }
  
  public static String decimalFormat(String price) {
    Double cny = Double.valueOf(Double.parseDouble(price));
    DecimalFormat df = new DecimalFormat("0.00");
    return df.format(cny);
  }
  
  public static double decimalScaleRoundUp(Integer scale, Object num) {
    BigDecimal bd = new BigDecimal(String.valueOf(num));
    return bd.setScale(scale.intValue(), 0).doubleValue();
  }
  
  public static int decimalScaleRoundUpInt(Integer scale, Object num) {
    BigDecimal bd = new BigDecimal(String.valueOf(num));
    return bd.setScale(scale.intValue(), 0).intValue();
  }
  
  public static BigDecimal decimalScaleBig(Integer scale, Object num) {
    BigDecimal bd = new BigDecimal(String.valueOf(num));
    return bd.setScale(scale.intValue(), 4);
  }
  
  public static double decimalScaleDouble(Integer scale, Double num) {
    BigDecimal bd = BigDecimal.valueOf(num.doubleValue());
    return bd.setScale(scale.intValue(), 4).doubleValue();
  }
  
  public static int decimalScaleInteger(Integer scale, Double num) {
    BigDecimal bd = BigDecimal.valueOf(num.doubleValue());
    return bd.setScale(scale.intValue(), 4).intValue();
  }
  
  public static int decimalScaleIntegerDown(Integer scale, Object num) {
    if (num == null)
      return 0; 
    BigDecimal bd = new BigDecimal(String.valueOf(num));
    return bd.setScale(scale.intValue(), 1).intValue();
  }
  
  public static BigDecimal decimalScaleDown(Integer scale, Object num) {
    if (num == null)
      return new BigDecimal(0); 
    BigDecimal bd = new BigDecimal(String.valueOf(num));
    return bd.setScale(scale.intValue(), 1);
  }
  
  public static double decimalScaleObjectDown(Integer scale, Object num) {
    if (num == null)
      return 0.0D; 
    BigDecimal bd = new BigDecimal(String.valueOf(num));
    return bd.setScale(scale.intValue(), 1).doubleValue();
  }
  
  public static double decimalFormat(String pattern, Double num) {
    DecimalFormat decimalFormat = new DecimalFormat(pattern);
    return Double.valueOf(decimalFormat.format(num)).doubleValue();
  }
  
  public static double doubleFormat(String format, Double strVal) {
    if (format == null || format.isEmpty() || format.equals("null"))
      format = "#.##"; 
    DecimalFormat df = new DecimalFormat(format);
    df.setRoundingMode(RoundingMode.DOWN);
    return Double.valueOf(df.format(strVal)).doubleValue();
  }
  
  public static int decimalScaleString(Integer scale, String num) {
    BigDecimal bd = new BigDecimal(num);
    return bd.setScale(scale.intValue(), 4).intValue();
  }
}
