
package com.kitty.common.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Collection;

public class ArraysUtil
{
    public static boolean contains(final Integer[] arr, final Integer targetValue) {
        for (final Integer s : arr) {
            if (s.equals(targetValue)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean contains(final String[] arr, final String targetValue) {
        for (final String s : arr) {
            if (s.equals(targetValue)) {
                return true;
            }
        }
        return false;
    }
    
    public static Collection getDifferListByMapPlus(final Collection collmax, final Collection collmin) {
        final Collection csReturn = new LinkedList();
        Collection max = collmax;
        Collection min = collmin;
        if (collmax.size() < collmin.size()) {
            max = collmin;
            min = collmax;
        }
        final Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
        for (final Object object : max) {
            map.put(object, 1);
        }
        for (final Object object : min) {
            if (map.get(object) == null) {
                csReturn.add(object);
            }
            else {
                map.put(object, 2);
            }
        }
        for (final Map.Entry<Object, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                csReturn.add(entry.getKey());
            }
        }
        return csReturn;
    }
    
    public static Collection getSameListByMap(final Collection collmax, final Collection collmin) {
        final Collection csReturn = new LinkedList();
        Collection max = collmax;
        Collection min = collmin;
        if (collmax.size() < collmin.size()) {
            max = collmin;
            min = collmax;
        }
        final Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
        for (final Object object : max) {
            map.put(object, 1);
        }
        for (final Object object : min) {
            if (map.get(object) != null) {
                csReturn.add(object);
            }
        }
        return csReturn;
    }
    
    public static Collection getDislodgeReDo(final Collection collA, final Collection collB) {
        final Collection csReturn = new LinkedList();
        final Map<Object, Integer> map = new HashMap<Object, Integer>(collA.size());
        for (final Object object : collA) {
            map.put(object, 1);
        }
        for (final Object object : collB) {
            if (map.get(object) != null) {
                map.remove(object);
            }
        }
        csReturn.addAll(Arrays.asList(map.keySet().toArray()));
        return csReturn;
    }
}
