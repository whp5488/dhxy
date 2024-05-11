
package com.kitty.common.utils;

import java.util.Comparator;
import com.google.common.collect.Maps;
import com.alibaba.fastjson.JSONObject;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.Map;

public class MapObjectUtil
{
    public static String getMapToString(final Map map) {
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
    
    public static String getMapToIntString(final Map map) {
        final Set<String> keySet = map.keySet();
        final Integer[] keyArray = keySet.toArray(new Integer[keySet.size()]);
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
        final Map<String, Object> map = new HashMap<String, Object>();
        if (!str.isEmpty()) {
            final String[] str2 = str.split(",");
            for (int i = 0; i < str2.length; ++i) {
                final String[] str3 = str2[i].split(":");
                map.put(str3[0], str3[1]);
            }
        }
        return map;
    }
    
    public static Map<String, Double> getStringToMapDouble(final String str) {
        final String[] str2 = str.split(",");
        final Map<String, Double> map = new HashMap<String, Double>();
        for (int i = 0; i < str2.length; ++i) {
            final String[] str3 = str2[i].split(":");
            map.put(str3[0], Double.valueOf(str3[1]));
        }
        return map;
    }
    
    public static Map<Integer, Integer> getStringToMapInteger(final String str) {
        final String[] str2 = str.split(",");
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < str2.length; ++i) {
            final String[] str3 = str2[i].split(":");
            map.put(Integer.valueOf(str3[0]), Integer.valueOf(str3[1]));
        }
        return map;
    }
    
    public static Map<String, Integer> getStringToMapStrInteger(final String str) {
        final Map<String, Integer> map = new HashMap<String, Integer>();
        if (!str.isEmpty()) {
            final String[] str2 = str.split(",");
            for (int i = 0; i < str2.length; ++i) {
                final String[] str3 = str2[i].split(":");
                map.put(str3[0], Integer.valueOf(str3[1]));
            }
        }
        return map;
    }
    
    public static Map<Integer, String> getStringToMapIntegerStr(final String str) {
        final String[] str2 = str.split(",");
        final Map<Integer, String> map = new HashMap<Integer, String>();
        for (int i = 0; i < str2.length; ++i) {
            final String[] str3 = str2[i].split(":");
            map.put(Integer.valueOf(str3[0]), str3[1]);
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
        final Map map = new HashMap();
        final String[] split = str.split(symbol);
        for (int i = 0; i < split.length; ++i) {
            final String strings = split[i].trim();
            final String[] data = strings.split(tow_symbol);
            map.put(data[0], data[1]);
        }
        return map;
    }
    
    public static String characterIdentityMap(final IdentityHashMap<String, Object> map) {
        String value = "";
        for (final String key : map.keySet()) {
            value = value + key + "-" + map.get(key) + " ";
        }
        return value.substring(0, value.length() - 1);
    }
    
    public static IdentityHashMap<String, Object> resolutionIdentityMap(final String str, final String symbol, final String tow_symbol) {
        final IdentityHashMap<String, Object> map = new IdentityHashMap<String, Object>();
        final String[] split = str.split(symbol);
        for (int i = 0; i < split.length; ++i) {
            final String strings = split[i].trim();
            final String[] data = strings.split(tow_symbol);
            map.put(data[0], data[1]);
        }
        return map;
    }
    
    public static Object mapToObject(final Map<String, Object> map, final Class<?> beanClass) throws Exception {
        final String jsonStr = JSONObject.toJSONString((Object)map);
        return JSONObject.parseObject(jsonStr, (Class)beanClass);
    }
    
    public static Map<String, Object> objectToMap(final Object obj) {
        final String jsonStr = JSONObject.toJSONString(obj);
        return (Map<String, Object>)JSONObject.parseObject(jsonStr);
    }
    
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(final Map<K, V> map, final boolean isDesc) {
        final Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            // 这里可能有bug
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByKey().reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        } else {
            map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        }
        return result;
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map, final boolean isDesc) {
        final Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted(Map.Entry.<K, V>comparingByValue().reversed()).forEachOrdered(e -> {
                final Comparable comparable = result.put(e.getKey(), e.getValue());
                return;
            });
        }
        else {
            map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> {
                final Comparable comparable2 = result.put(e.getKey(), e.getValue());
                return;
            });
        }
        return result;
    }
}
