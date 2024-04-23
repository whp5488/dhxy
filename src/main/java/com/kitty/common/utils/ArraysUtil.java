package com.kitty.common.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class ArraysUtil {
  public static boolean contains(Integer[] arr, Integer targetValue) {
    for (Integer s : arr) {
      if (s.equals(targetValue))
        return true; 
    } 
    return false;
  }
  
  public static boolean contains(String[] arr, String targetValue) {
    for (String s : arr) {
      if (s.equals(targetValue))
        return true; 
    } 
    return false;
  }
  
  public static Collection getDifferListByMapPlus(Collection collmax, Collection collmin) {
    Collection<Object> csReturn = new LinkedList();
    Collection max = collmax;
    Collection min = collmin;
    if (collmax.size() < collmin.size()) {
      max = collmin;
      min = collmax;
    } 
    Map<Object, Integer> map = new HashMap<>(max.size());
    for (Object object : max)
      map.put(object, Integer.valueOf(1)); 
    for (Object object : min) {
      if (map.get(object) == null) {
        csReturn.add(object);
        continue;
      } 
      map.put(object, Integer.valueOf(2));
    } 
    for (Map.Entry<Object, Integer> entry : map.entrySet()) {
      if (((Integer)entry.getValue()).intValue() == 1)
        csReturn.add(entry.getKey()); 
    } 
    return csReturn;
  }
  
  public static Collection getSameListByMap(Collection collmax, Collection collmin) {
    Collection<Object> csReturn = new LinkedList();
    Collection max = collmax;
    Collection min = collmin;
    if (collmax.size() < collmin.size()) {
      max = collmin;
      min = collmax;
    } 
    Map<Object, Integer> map = new HashMap<>(max.size());
    for (Object object : max)
      map.put(object, Integer.valueOf(1)); 
    for (Object object : min) {
      if (map.get(object) != null)
        csReturn.add(object); 
    } 
    return csReturn;
  }
  
  public static Collection getDislodgeReDo(Collection collA, Collection collB) {
    Collection csReturn = new LinkedList();
    Map<Object, Integer> map = new HashMap<>(collA.size());
    for (Object object : collA)
      map.put(object, Integer.valueOf(1)); 
    for (Object object : collB) {
      if (map.get(object) != null)
        map.remove(object); 
    } 
    csReturn.addAll(Arrays.asList(map.keySet().toArray()));
    return csReturn;
  }
}
