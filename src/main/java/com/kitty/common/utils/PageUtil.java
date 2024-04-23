package com.kitty.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageUtil {
  public static <T> List<T> subList(List<T> list, Integer page, Integer pageSize) {
    if (list == null)
      return new ArrayList<>(); 
    if (list.size() == 0)
      return new ArrayList<>(); 
    return (List<T>)list.stream().skip(((page.intValue() - 1) * pageSize.intValue())).limit(pageSize.intValue()).collect(Collectors.toList());
  }
}
