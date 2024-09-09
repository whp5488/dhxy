package com.kitty.common.utils.text;

import java.util.Set;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.text.NumberFormat;
import com.kitty.common.utils.StringUtils;

public class Convert
{
    public Convert() {
    }
    
    public static String toStr(Object value, String defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if ((value instanceof String)) {
            return (String)value;
        }
        return value.toString();
    }
    
    public static String toStr(Object value) {
        return toStr(value, null);
    }
    
    public static Character toChar(Object value, Character defaultValue) {
        if (null == value) {
            return defaultValue;
        }
        if ((value instanceof Character)) {
            return (Character)value;
        }
        String valueStr = toStr(value, null);
        return Character.valueOf(StringUtils.isEmpty(valueStr) ? ((char)defaultValue) : valueStr.charAt(0));
    }
    
    public static Character toChar(Object value) {
        return toChar(value, null);
    }
    
    public static Byte toByte(Object value, Byte defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Byte)) {
            return (Byte)value;
        }
        if ((value instanceof Number)) {
            return Byte.valueOf(((Number)value).byteValue());
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Byte.valueOf(Byte.parseByte(valueStr));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Byte toByte(Object value) {
        return toByte(value, null);
    }
    
    public static Short toShort(Object value, Short defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Short)) {
            return (Short)value;
        }
        if ((value instanceof Number)) {
            return Short.valueOf(((Number)value).shortValue());
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Short.valueOf(Short.parseShort(valueStr.trim()));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Short toShort(Object value) {
        return toShort(value, null);
    }
    
    public static Number toNumber(Object value, Number defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Number)) {
            return (Number)value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return NumberFormat.getInstance().parse(valueStr);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Number toNumber(Object value) {
        return toNumber(value, null);
    }
    
    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Integer)) {
            return (Integer)value;
        }
        if ((value instanceof Number)) {
            return Integer.valueOf(((Number)value).intValue());
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(Integer.parseInt(valueStr.trim()));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Integer toInt(Object value) {
        return toInt(value, null);
    }
    
    public static Integer[] toIntArray(String str) {
        return toIntArray(",", str);
    }
    
    public static Long[] toLongArray(String str) {
        return toLongArray(",", str);
    }
    
    public static Integer[] toIntArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Integer[0];
        }
        String[] arr = str.split(split);
        Integer[] ints = new Integer[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            Integer v = toInt(arr[i], Integer.valueOf(0));
            ints[i] = v;
        }
        return ints;
    }
    
    public static Long[] toLongArray(String split, String str) {
        if (StringUtils.isEmpty(str)) {
            return new Long[0];
        }
        String[] arr = str.split(split);
        Long[] longs = new Long[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            Long v = toLong(arr[i], null);
            longs[i] = v;
        }
        return longs;
    }
    
    public static String[] toStrArray(String str) {
        return toStrArray(",", str);
    }
    
    public static String[] toStrArray(String split, String str) {
        return str.split(split);
    }
    
    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Long)) {
            return (Long)value;
        }
        if ((value instanceof Number)) {
            return Long.valueOf(((Number)value).longValue());
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Long.valueOf(new BigDecimal(valueStr.trim()).longValue());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Long toLong(Object value) {
        return toLong(value, null);
    }
    
    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Double)) {
            return (Double)value;
        }
        if ((value instanceof Number)) {
            return Double.valueOf(((Number)value).doubleValue());
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Double.valueOf(new BigDecimal(valueStr.trim()).doubleValue());
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Double toDouble(Object value) {
        return toDouble(value, null);
    }
    
    public static Float toFloat(Object value, Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Float)) {
            return (Float)value;
        }
        if ((value instanceof Number)) {
            return Float.valueOf(((Number)value).floatValue());
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Float.valueOf(Float.parseFloat(valueStr.trim()));
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static Float toFloat(Object value) {
        return toFloat(value, null);
    }
    
    public static Boolean toBool(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof Boolean)) {
            return (Boolean)value;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        valueStr = (valueStr = valueStr.trim().toLowerCase());
        int n = -1;
        switch (valueStr.hashCode()) {
            case 3569038: {
                if (valueStr.equals("true")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 97196323: {
                if (valueStr.equals("false")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 119527: {
                if (valueStr.equals("yes")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 3548: {
                if (valueStr.equals("ok")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 3521: {
                if (valueStr.equals("no")) {
                    n = 4;
                    break;
                }
                break;
            }
            case 49: {
                if (valueStr.equals("1")) {
                    n = 5;
                    break;
                }
                break;
            }
            case 48: {
                if (valueStr.equals("0")) {
                    n = 6;
                    break;
                }
                break;
            }
        }
        switch (n) {
            case 0: {
                return Boolean.valueOf(true);
            }
            case 1: {
                return Boolean.valueOf(false);
            }
            case 2: {
                return Boolean.valueOf(true);
            }
            case 3: {
                return Boolean.valueOf(true);
            }
            case 4: {
                return Boolean.valueOf(false);
            }
            case 5: {
                return Boolean.valueOf(true);
            }
            case 6: {
                return Boolean.valueOf(false);
            }
            default: {
                return defaultValue;
            }
        }
    }
    
    public static Boolean toBool(Object value) {
        return toBool(value, null);
    }
    
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value, E defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (clazz.isAssignableFrom(value.getClass())) {
            E myE = (E)value;
            return myE;
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return Enum.valueOf(clazz, valueStr);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static <E extends Enum<E>> E toEnum(Class<E> clazz, Object value) {
        return toEnum(clazz, value, null);
    }
    
    public static BigInteger toBigInteger(Object value, BigInteger defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof BigInteger)) {
            return (BigInteger)value;
        }
        if ((value instanceof Long)) {
            return BigInteger.valueOf((long)value);
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigInteger(valueStr);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static BigInteger toBigInteger(Object value) {
        return toBigInteger(value, null);
    }
    
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if ((value instanceof BigDecimal)) {
            return (BigDecimal)value;
        }
        if ((value instanceof Long)) {
            return new BigDecimal((long)value);
        }
        if ((value instanceof Double)) {
            return new BigDecimal((double)value);
        }
        if ((value instanceof Integer)) {
            return new BigDecimal((int)value);
        }
        String valueStr = toStr(value, null);
        if (StringUtils.isEmpty(valueStr)) {
            return defaultValue;
        }
        try {
            return new BigDecimal(valueStr);
        }
        catch (Exception e) {
            return defaultValue;
        }
    }
    
    public static BigDecimal toBigDecimal(Object value) {
        return toBigDecimal(value, null);
    }
    
    public static String utf8Str(Object obj) {
        return str(obj, CharsetKit.CHARSET_UTF_8);
    }
    
    public static String str(Object obj, String charsetName) {
        return str(obj, Charset.forName(charsetName));
    }
    
    public static String str(Object obj, Charset charset) {
        if (null == obj) {
            return null;
        }
        if ((obj instanceof String)) {
            return (String)obj;
        }
        if (obj instanceof byte[] || obj instanceof Byte[]) {
            return str((Byte[])(Byte[])obj, charset);
        }
        if ((obj instanceof ByteBuffer)) {
            return str((ByteBuffer)obj, charset);
        }
        return obj.toString();
    }
    
    public static String str(byte[] bytes, String charset) {
        return str(bytes, StringUtils.isEmpty(charset) ? Charset.defaultCharset() : Charset.forName(charset));
    }
    
    public static String str(byte[] data, Charset charset) {
        if (data == null) {
            return null;
        }
        if (null == charset) {
            return new String(data);
        }
        return new String(data, charset);
    }
    
    public static String str(ByteBuffer data, String charset) {
        if (data == null) {
            return null;
        }
        return str(data, Charset.forName(charset));
    }
    
    public static String str(ByteBuffer data, Charset charset) {
        if (null == charset) {
            charset = Charset.defaultCharset();
        }
        return charset.decode(data).toString();
    }
    
    public static String toSBC(String input) {
        return toSBC(input, null);
    }
    
    public static String toSBC(String input, Set<Character> notConvertSet) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            if (null == notConvertSet || !notConvertSet.contains(Character.valueOf(c[i]))) {
                if (c[i] == ' ') {
                    c[i] = '\u3000';
                }
                else if (c[i] < '\u007f') {
                    c[i] += 'ﻠ';
                }
            }
        }
        return new String(c);
    }
    
    public static String toDBC(String input) {
        return toDBC(input, null);
    }
    
    public static String toDBC(String text, Set<Character> notConvertSet) {
        char[] c = text.toCharArray();
        for (int i = 0; i < c.length; ++i) {
            if (null == notConvertSet || !notConvertSet.contains(Character.valueOf(c[i]))) {
                if (c[i] == '\u3000') {
                    c[i] = ' ';
                }
                else if (c[i] > '\uff00' && c[i] < '｟') {
                    c[i] -= 'ﻠ';
                }
            }
        }
        String returnString = new String(c);
        return returnString;
    }
    
    public static String digitUppercase(double n) {
        String[] fraction = { "角", "分" };
        String[] digit = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String[][] unit = { { "元", "万", "亿" }, { "", "拾", "佰", "仟" } };
        String head = (n < 0.0) ? "负" : "";
        n = Math.abs(n);
        String s = "";
        for (int i = 0; i < fraction.length; ++i) {
            s += (digit[(int)(Math.floor(n * 10.0 * Math.pow(10.0, (double)i)) % 10.0)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if (s.length() < 1) {
            s = "整";
        }
        for (int integerPart = (int)Math.floor(n), j = 0; j < unit[0].length && integerPart > 0; ++j) {
            String p = "";
            for (int k = 0; k < unit[1].length && n > 0.0; ++k) {
                p = digit[integerPart % 10] + unit[1][k] + p;
                integerPart /= 10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][j] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
    public static String chineseNumberPlusOne(String chineseNumber) {
        Integer aFromC = Chinese.getAFromC(chineseNumber);
        String cFromA = Chinese.getCFromA((int)aFromC + 1);
        return cFromA;
    }
    
    public static Integer getArabicFromChinese(String chineseValue) {
        return Chinese.getAFromC(chineseValue);
    }
    
    public static String getChineseFromArabic(Integer arabic) {
        return Chinese.getCFromA((int)arabic);
    }
    
    public static String getArabicFromWeek(String arabic) {
        if ("7".equals(arabic)) {
            return "日";
        }
        return Chinese.getCFromA((int)Integer.valueOf(arabic));
    }
}
