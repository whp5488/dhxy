package com.kitty.common.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collection;

public class ArraysUtil
{
    public ArraysUtil() {
    }
    
    public static boolean contains(Integer[] arr, Integer targetValue) {
        int length = arr.length;
        int i = 0;
        while (i < length) {
            Integer s = arr[i];
            if (s.equals(targetValue)) {
                return true;
            }
            ++i;
        }
        return false;
    }
    
    public static boolean contains(String[] arr, String targetValue) {
        int length = arr.length;
        int i = 0;
        while (i < length) {
            String s = arr[i];
            if (s.equals(targetValue)) {
                return true;
            }
            ++i;
        }
        return false;
    }
    
    public static Collection getDifferListByMapPlus(Collection collmax, Collection collmin) {
        Collection<Object> csReturn = new LinkedList();
        Collection<Object> max = collmax;
        Collection<Object> min = collmin;
        if (collmax.size() < collmin.size()) {
            max = collmin;
            min = collmax;
        }
        Map<Object, Integer> map = new HashMap(max.size());
        for (Object object : max) {
            map.put(object, Integer.valueOf(1));
        }
        for (Object object : min) {
            if (map.get(object) == null) {
                csReturn.add(object);
            }
            else {
                map.put(object, Integer.valueOf(2));
            }
        }
        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            if ((int)entry.getValue() == 1) {
                csReturn.add(entry.getKey());
            }
            else {
                continue;
            }
        }
        return csReturn;
    }
    
    public static Collection getSameListByMap(Collection collmax, Collection collmin) {
        Collection<Object> csReturn = new LinkedList();
        Collection<Object> max = collmax;
        Collection<Object> min = collmin;
        if (collmax.size() < collmin.size()) {
            max = collmin;
            min = collmax;
        }
        Map<Object, Integer> map = new HashMap(max.size());
        for (Object object : max) {
            map.put(object, Integer.valueOf(1));
        }
        for (Object object : min) {
            if (map.get(object) != null) {
                csReturn.add(object);
            }
            else {
                continue;
            }
        }
        return csReturn;
    }
    
    public static Collection getDislodgeReDo(Collection collA, Collection collB) {
        Collection<Object> csReturn = new LinkedList();
        Map<Object, Integer> map = new HashMap(collA.size());
        for (Object object : collA) {
            map.put(object, Integer.valueOf(1));
        }
        for (Object object : collB) {
            if (map.get(object) != null) {
                map.remove(object);
            }
            else {
                continue;
            }
        }
        csReturn.addAll(Arrays.asList(map.keySet().toArray()));
        return csReturn;
    }
}
