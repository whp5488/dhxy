package com.kitty.common.utils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

public class MapObjectUtil
{
    public MapObjectUtil() {
    }
    
    public static String getMapToString(Map map) {
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
    
    public static String getMapToIntString(Map map) {
        Set<String> keySet = map.keySet();
        Integer[] keyArray = (Integer[])keySet.toArray(new Integer[keySet.size()]);
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
        Map<String, Object> map = new HashMap();
        if (!str.isEmpty()) {
            String[] str2 = str.split(",");
            for (int i = 0; i < str2.length; ++i) {
                String[] str3 = str2[i].split(":");
                map.put(str3[0], str3[1]);
            }
            return map;
        }
        return map;
    }
    
    public static Map<String, Double> getStringToMapDouble(String str) {
        String[] str2 = str.split(",");
        Map<String, Double> map = new HashMap();
        for (int i = 0; i < str2.length; ++i) {
            String[] str3 = str2[i].split(":");
            map.put(str3[0], Double.valueOf(str3[1]));
        }
        return map;
    }
    
    public static Map<Integer, Integer> getStringToMapInteger(String str) {
        String[] str2 = str.split(",");
        Map<Integer, Integer> map = new HashMap();
        for (int i = 0; i < str2.length; ++i) {
            String[] str3 = str2[i].split(":");
            map.put(Integer.valueOf(str3[0]), Integer.valueOf(str3[1]));
        }
        return map;
    }
    
    public static Map<String, Integer> getStringToMapStrInteger(String str) {
        Map<String, Integer> map = new HashMap();
        if (!str.isEmpty()) {
            String[] str2 = str.split(",");
            for (int i = 0; i < str2.length; ++i) {
                String[] str3 = str2[i].split(":");
                map.put(str3[0], Integer.valueOf(str3[1]));
            }
            return map;
        }
        return map;
    }
    
    public static Map<Integer, String> getStringToMapIntegerStr(String str) {
        String[] str2 = str.split(",");
        Map<Integer, String> map = new HashMap();
        for (int i = 0; i < str2.length; ++i) {
            String[] str3 = str2[i].split(":");
            map.put(Integer.valueOf(str3[0]), str3[1]);
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
    
    public static String characterIdentityMap(IdentityHashMap<String, Object> map) {
        String value = "";
        for (String key : map.keySet()) {
            value = value + key + "-" + map.get(key) + " ";
        }
        return value.substring(0, value.length() - 1);
    }
    
    public static IdentityHashMap<String, Object> resolutionIdentityMap(String str, String symbol, String tow_symbol) {
        IdentityHashMap<String, Object> map = new IdentityHashMap();
        String[] split = str.split(symbol);
        for (int i = 0; i < split.length; ++i) {
            String strings = split[i].trim();
            String[] data = strings.split(tow_symbol);
            map.put(data[0], data[1]);
        }
        return map;
    }
    
    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        String jsonStr = JSONObject.toJSONString(map);
        return JSONObject.parseObject(jsonStr, beanClass);
    }
    
    public static Map<String, Object> objectToMap(Object obj) {
        String jsonStr = JSONObject.toJSONString(obj);
        return JSONObject.parseObject(jsonStr);
    }
    
    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted((Comparator)Map.Entry.comparingByKey().reversed()).forEachOrdered(e -> result.put((K)((Map.Entry)e).getKey(), (V)((Map.Entry)e).getValue()));
            return result;
        }
        map.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }
    
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = Maps.newLinkedHashMap();
        if (isDesc) {
            map.entrySet().stream().sorted((Comparator)Map.Entry.comparingByValue().reversed()).forEachOrdered(e -> {
                Comparable comparable2 = (Comparable)result.put((K)((Map.Entry)e).getKey(), (V)((Map.Entry)e).getValue());
                return;
            });
            return result;
        }
        map.entrySet().stream().sorted(Map.Entry.comparingByValue()).forEachOrdered(e -> {
            Comparable comparable = (Comparable)result.put(e.getKey(), e.getValue());
            return;
        });
        return result;
    }
}
