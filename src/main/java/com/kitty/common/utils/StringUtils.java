
package com.kitty.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.kitty.common.utils.text.StrFormatter;
import java.util.Map;
import java.util.Collection;

public class StringUtils extends org.apache.commons.lang3.StringUtils
{
    private static final String NULLSTR = "";
    private static final char SEPARATOR = '_';
    
    public static <T> T nvl(final T value, final T defaultValue) {
        return (value != null) ? value : defaultValue;
    }
    
    public static boolean isEmpty(final Collection<?> coll) {
        return isNull(coll) || coll.isEmpty();
    }
    
    public static boolean isNotEmpty(final Collection<?> coll) {
        return !isEmpty(coll);
    }
    
    public static boolean isEmpty(final Object[] objects) {
        return isNull(objects) || objects.length == 0;
    }
    
    public static boolean isNotEmpty(final Object[] objects) {
        return !isEmpty(objects);
    }
    
    public static boolean isEmpty(final Map<?, ?> map) {
        return isNull(map) || map.isEmpty();
    }
    
    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }
    
    public static boolean isEmpty(final String str) {
        return isNull(str) || "".equals(str.trim());
    }
    
    public static boolean isNotEmpty(final String str) {
        return !isEmpty(str);
    }
    
    public static boolean isNull(final Object object) {
        return object == null;
    }
    
    public static boolean isNotNull(final Object object) {
        return !isNull(object);
    }
    
    public static boolean isArray(final Object object) {
        return isNotNull(object) && object.getClass().isArray();
    }
    
    public static String trim(final String str) {
        return (str == null) ? "" : str.trim();
    }
    
    public static String substring(final String str, int start) {
        if (str == null) {
            return "";
        }
        if (start < 0) {
            start += str.length();
        }
        if (start < 0) {
            start = 0;
        }
        if (start > str.length()) {
            return "";
        }
        return str.substring(start);
    }
    
    public static String substring(final String str, int start, int end) {
        if (str == null) {
            return "";
        }
        if (end < 0) {
            end += str.length();
        }
        if (start < 0) {
            start += str.length();
        }
        if (end > str.length()) {
            end = str.length();
        }
        if (start > end) {
            return "";
        }
        if (start < 0) {
            start = 0;
        }
        if (end < 0) {
            end = 0;
        }
        return str.substring(start, end);
    }
    
    public static String format(final String template, final Object... params) {
        if (isEmpty(params) || isEmpty(template)) {
            return template;
        }
        return StrFormatter.format(template, params);
    }
    
    public static String toUnderScoreCase(final String str) {
        if (str == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        boolean preCharIsUpperCase = true;
        boolean curreCharIsUpperCase = true;
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < str.length(); ++i) {
            final char c = str.charAt(i);
            preCharIsUpperCase = (i > 0 && Character.isUpperCase(str.charAt(i - 1)));
            curreCharIsUpperCase = Character.isUpperCase(c);
            if (i < str.length() - 1) {
                nexteCharIsUpperCase = Character.isUpperCase(str.charAt(i + 1));
            }
            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append('_');
            }
            else if (i != 0 && !preCharIsUpperCase && curreCharIsUpperCase) {
                sb.append('_');
            }
            sb.append(Character.toLowerCase(c));
        }
        return sb.toString();
    }
    
    public static boolean inStringIgnoreCase(final String str, final String... strs) {
        if (str != null && strs != null) {
            for (final String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static String convertToCamelCase(final String name) {
        final StringBuilder result = new StringBuilder();
        if (name == null || name.isEmpty()) {
            return "";
        }
        if (!name.contains("_")) {
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        final String[] split;
        final String[] camels = split = name.split("_");
        for (final String camel : split) {
            if (!camel.isEmpty()) {
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }
    
    public static String toCamelCase(String s) {
        if (s == null) {
            return null;
        }
        if (s.indexOf(95) == -1) {
            return s;
        }
        s = s.toLowerCase();
        final StringBuilder sb = new StringBuilder(s.length());
        boolean upperCase = false;
        for (int i = 0; i < s.length(); ++i) {
            final char c = s.charAt(i);
            if (c == '_') {
                upperCase = true;
            }
            else if (upperCase) {
                sb.append(Character.toUpperCase(c));
                upperCase = false;
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static <T> T cast(final Object obj) {
        return (T)obj;
    }
    
    public static String deleteStringFront(String string, final String str, final int num) {
        if (string.contains(str)) {
            string = string.substring(string.indexOf(str) + num);
        }
        return string;
    }
    
    public static String deleteStringAfter(String string, final String str) {
        if (string.contains(str)) {
            string = string.substring(0, string.indexOf(str));
        }
        return string;
    }
    
    public static boolean isChineseCharacter(final String input) {
        if (input == null) {
            return false;
        }
        final String regex = "^[一-龥]$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
