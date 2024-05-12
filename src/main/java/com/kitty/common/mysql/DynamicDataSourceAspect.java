
package com.kitty.common.mysql;

import org.slf4j.LoggerFactory;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect
{
    private static final Logger logger;
    
    @Before("@annotation(ds)")
    public void changeDataSource(final JoinPoint point, final TargetDataSource ds) throws Throwable {
        final String dsId = ds.name();
        if (!DynamicDataSourceContextHolder.containsDataSource(dsId)) {
            DynamicDataSourceAspect.logger.error("数据源[{}]不存在，使用默认数据源 > {}", ds.name(), point.getSignature());
        }
        else {
            DynamicDataSourceAspect.logger.debug("Use DataSource : {} > {}", ds.name(), point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceType(ds.name());
        }
    }
    
    @After("@annotation(ds)")
    public void restoreDataSource(final JoinPoint point, final TargetDataSource ds) {
        DynamicDataSourceAspect.logger.debug("Revert DataSource : {} > {}", ds.name(), point.getSignature());
        DynamicDataSourceContextHolder.clearDataSourceType();
    }
    
    static {
        logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    }
}
