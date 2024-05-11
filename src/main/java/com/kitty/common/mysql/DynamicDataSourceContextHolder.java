
package com.kitty.common.mysql;

import java.util.ArrayList;
import java.util.List;

public class DynamicDataSourceContextHolder
{
    private static final ThreadLocal<String> contextHolder;
    public static List<String> dataSourceIds;
    
    public static void setDataSourceType(final String dataSourceType) {
        DynamicDataSourceContextHolder.contextHolder.set(dataSourceType);
    }
    
    public static String getDataSourceType() {
        return DynamicDataSourceContextHolder.contextHolder.get();
    }
    
    public static void clearDataSourceType() {
        DynamicDataSourceContextHolder.contextHolder.remove();
    }
    
    public static boolean containsDataSource(final String dataSourceId) {
        return DynamicDataSourceContextHolder.dataSourceIds.contains(dataSourceId);
    }
    
    static {
        contextHolder = new ThreadLocal<String>();
        DynamicDataSourceContextHolder.dataSourceIds = new ArrayList<String>();
    }
}
