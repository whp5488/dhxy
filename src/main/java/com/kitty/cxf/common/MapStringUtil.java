
package com.kitty.cxf.common;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.Map;

public class MapStringUtil
{
    public static String getMapToString(final Map<String, Object> map) {
        final Set<String> keySet = map.keySet();
        final String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; ++i) {
            if (String.valueOf(map.get(keyArray[i])).trim().length() > 0) {
                sb.append(keyArray[i]).append(":").append(String.valueOf(map.get(keyArray[i])).trim());
            }
            if (i != keyArray.length - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }
    
    public static Map<String, Object> getStringToMap(final String str) {
        final String[] str2 = str.split(",");
        final Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < str2.length; ++i) {
            final String[] str3 = str2[i].split(":");
            map.put(str3[0], str3[1]);
        }
        return map;
    }
    
    public static String character(final Map<String, Object> map) {
        String value = "";
        for (final String key : map.keySet()) {
            value = value + key + "-" + map.get(key) + " ";
        }
        return value.substring(0, value.length() - 1);
    }
    
    public static Map<String, Object> resolution(final String str, final String symbol, final String tow_symbol) {
        final Map<String, Object> map = new HashMap<>();
        final String[] split = str.split(symbol);
        for (int i = 0; i < split.length; ++i) {
            final String strings = split[i].trim();
            final String[] data = strings.split(tow_symbol);
            map.put(data[0], data[1]);
        }
        return map;
    }
}
