package com.kitty.common.mysql;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect
{
    private Logger logger;
    
    public DynamicDataSourceAspect() {
        this.logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    }
    
    @Before("@annotation(ds)")
    public void changeDataSource(JoinPoint point, TargetDataSource ds) throws Throwable {
        String dsId = ds.name();
        if (DynamicDataSourceContextHolder.isContainsDataSource(dsId)) {
            DynamicDataSourceContextHolder.setDataSourceType(ds.name());
        }
    }
    
    @After("@annotation(ds)")
    public void restoreDataSource(JoinPoint point, TargetDataSource ds) {
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
}
