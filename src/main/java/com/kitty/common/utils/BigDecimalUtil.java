
package com.kitty.common.utils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.math.BigDecimal;

public class BigDecimalUtil
{
    public static String addition(final String one, final String two) {
        final BigDecimal decimal_one = new BigDecimal(one);
        final BigDecimal decimal_two = new BigDecimal(two);
        return decimal_one.add(decimal_two).stripTrailingZeros().toPlainString();
    }
    
    public static double additionDouble(final double one, final double two) {
        final String resouce = addition(String.valueOf(one), String.valueOf(two));
        return Double.valueOf(resouce);
    }
    
    public static String subtract(final String one, final String two) {
        final BigDecimal decimal_one = new BigDecimal(one);
        final BigDecimal decimal_two = new BigDecimal(two);
        return decimal_one.subtract(decimal_two).stripTrailingZeros().toPlainString();
    }
    
    public static String multiply(final String one, final String two) {
        final BigDecimal decimal_one = new BigDecimal(one);
        final BigDecimal decimal_two = new BigDecimal(two);
        return decimal_one.multiply(decimal_two).stripTrailingZeros().toPlainString();
    }
    
    public static String divide(final String one, final String two) {
        final String decimal = "0";
        if (decimal.equals(two)) {
            return decimal;
        }
        final BigDecimal decimal_one = new BigDecimal(one);
        final BigDecimal decimal_two = new BigDecimal(two);
        return decimal_one.divide(decimal_two, 2, 4).stripTrailingZeros().toPlainString();
    }
    
    public static String divide(final String one, final String two, final int scale) {
        final String decimal = "0";
        if (decimal.equals(two)) {
            return decimal;
        }
        final BigDecimal decimal_one = new BigDecimal(one);
        final BigDecimal decimal_two = new BigDecimal(two);
        return decimal_one.divide(decimal_two, scale, 4).stripTrailingZeros().toPlainString();
    }
    
    public static String whole(final String one) {
        final int floor = (int)Math.floor(Double.parseDouble(one));
        return String.valueOf(floor);
    }
    
    public static String decimalFormat(final String price) {
        final Double cny = Double.parseDouble(price);
        final DecimalFormat df = new DecimalFormat("0.00");
        return df.format(cny);
    }
    
    public static double decimalScaleRoundUp(final Integer scale, final Object num) {
        final BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.setScale(scale, 0).doubleValue();
    }
    
    public static int decimalScaleRoundUpInt(final Integer scale, final Object num) {
        final BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.setScale(scale, 0).intValue();
    }
    
    public static BigDecimal decimalScaleBig(final Integer scale, final Object num) {
        final BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.setScale(scale, 4);
    }
    
    public static double decimalScaleDouble(final Integer scale, final Double num) {
        final BigDecimal bd = BigDecimal.valueOf(num);
        return bd.setScale(scale, 4).doubleValue();
    }
    
    public static int decimalScaleInteger(final Integer scale, final Double num) {
        final BigDecimal bd = BigDecimal.valueOf(num);
        return bd.setScale(scale, 4).intValue();
    }
    
    public static int decimalScaleIntegerDown(final Integer scale, final Object num) {
        if (num == null) {
            return 0;
        }
        final BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.setScale(scale, 1).intValue();
    }
    
    public static BigDecimal decimalScaleDown(final Integer scale, final Object num) {
        if (num == null) {
            return new BigDecimal(0);
        }
        final BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.setScale(scale, 1);
    }
    
    public static double decimalScaleObjectDown(final Integer scale, final Object num) {
        if (num == null) {
            return 0.0;
        }
        final BigDecimal bd = new BigDecimal(String.valueOf(num));
        return bd.setScale(scale, 1).doubleValue();
    }
    
    public static double decimalFormat(final String pattern, final Double num) {
        final DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return Double.valueOf(decimalFormat.format(num));
    }
    
    public static double doubleFormat(String format, final Double strVal) {
        if (format == null || format.isEmpty() || format.equals("null")) {
            format = "#.##";
        }
        final DecimalFormat df = new DecimalFormat(format);
        df.setRoundingMode(RoundingMode.DOWN);
        return Double.valueOf(df.format(strVal));
    }
    
    public static int decimalScaleString(final Integer scale, final String num) {
        final BigDecimal bd = new BigDecimal(num);
        return bd.setScale(scale, 4).intValue();
    }
}
