package com.kitty.cxf.common;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.Map;

public class MapStringUtil
{
    public MapStringUtil() {
    }
    
    public static String getMapToString(Map<String, Object> map) {
        Set<String> keySet = map.keySet();
        String[] keyArray = (String[])keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
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
    
    public static Map<String, Object> getStringToMap(String str) {
        String[] str2 = str.split(",");
        Map<String, Object> map = new HashMap();
        for (int i = 0; i < str2.length; ++i) {
            String[] str3 = str2[i].split(":");
            map.put(str3[0], str3[1]);
        }
        return map;
    }
    
    public static String character(Map<String, Object> map) {
        String value = "";
        for (String key : map.keySet()) {
            value = value + key + "-" + map.get(key) + " ";
        }
        return value.substring(0, value.length() - 1);
    }
    
    public static Map<String, Object> resolution(String str, String symbol, String tow_symbol) {
        Map<String, Object> map = new HashMap();
        String[] split = str.split(symbol);
        for (int i = 0; i < split.length; ++i) {
            String strings = split[i].trim();
            String[] data = strings.split(tow_symbol);
            map.put(data[0], data[1]);
        }
        return map;
    }
}
