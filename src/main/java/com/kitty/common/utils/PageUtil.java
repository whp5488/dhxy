
package com.kitty.common.utils;

import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

public class PageUtil
{
    public static <T> List<T> subList(final List<T> list, final Integer page, final Integer pageSize) {
        if (list == null) {
            return new ArrayList<T>();
        }
        if (list.size() == 0) {
            return new ArrayList<T>();
        }
        return list.stream().skip((page - 1) * pageSize).limit(pageSize).collect(Collectors.toList());
    }
}
