package com.kitty.common.mysql;

import java.util.ArrayList;
import org.slf4j.LoggerFactory;
import java.util.List;
import org.slf4j.Logger;

public class DynamicDataSourceContextHolder
{
    private Logger logger;
    private static final ThreadLocal<String> contextHolder;
    public static List<String> dataSourceIds;
    
    public DynamicDataSourceContextHolder() {
        this.logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);
    }
    
    public static void setDataSourceType(String dataSourceType) {
        DynamicDataSourceContextHolder.contextHolder.set(dataSourceType);
    }
    
    public static String getDataSourceType() {
        return (String)DynamicDataSourceContextHolder.contextHolder.get();
    }
    
    public static void clearDataSourceType() {
        DynamicDataSourceContextHolder.contextHolder.remove();
    }
    
    public static boolean isContainsDataSource(String dataSourceId) {
        return DynamicDataSourceContextHolder.dataSourceIds.contains(dataSourceId);
    }
    
    static {
        contextHolder = new ThreadLocal();
        DynamicDataSourceContextHolder.dataSourceIds = new ArrayList<>();
    }
}
