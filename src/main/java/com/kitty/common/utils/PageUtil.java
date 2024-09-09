package com.kitty.common.utils;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.List;

public class PageUtil
{
    public PageUtil() {
    }
    
    public static <T> List<T> subList(List<T> list, Integer page, Integer pageSize) {
        if (list == null) {
            return new ArrayList<>();
        }
        if (list.size() == 0) {
            return new ArrayList<>();
        }
        return (List)list.stream().skip((long)(((int)page - 1) * (int)pageSize)).limit((long)(int)pageSize).collect(Collectors.toList());
    }
}
