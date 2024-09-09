package com.kitty.common.mysql;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.beans.MutablePropertyValues;
import java.util.Iterator;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;
import com.kitty.common.utils.StringUtils;
import org.springframework.core.env.Environment;
import java.util.HashMap;
import org.slf4j.LoggerFactory;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware
{
    private Logger logger;
    private static final String DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources;
    
    public DynamicDataSourceRegister() {
        this.logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
        this.customDataSources = new HashMap();
    }
    
    public void setEnvironment(Environment environment) {
        this.initDefaultDataSource(environment);
        this.initCustomDataSources(environment);
    }
    
    private void initDefaultDataSource(Environment env) {
        Map<String, Object> dsMap = new HashMap();
        dsMap.put("driver", env.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("url", env.getProperty("spring.datasource.url"));
        dsMap.put("username", env.getProperty("spring.datasource.username"));
        dsMap.put("password", env.getProperty("spring.datasource.password"));
        this.defaultDataSource = this.buildDataSource(dsMap);
    }
    
    private void initCustomDataSources(Environment env) {
        String dsPrefixs = env.getProperty("custom.datasource.names");
        if (StringUtils.isNotBlank(dsPrefixs)) {
            for (String dsPrefix : dsPrefixs.split(",")) {
                Map<String, Object> dsMap = new HashMap();
                dsMap.put("driver", env.getProperty("custom.datasource." + dsPrefix + ".driver-class-name"));
                dsMap.put("url", env.getProperty("custom.datasource." + dsPrefix + ".url"));
                dsMap.put("username", env.getProperty("custom.datasource." + dsPrefix + ".username"));
                dsMap.put("password", env.getProperty("custom.datasource." + dsPrefix + ".password"));
                DataSource ds = this.buildDataSource(dsMap);
                this.customDataSources.put(dsPrefix, ds);
            }
        }
    }
    
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        Map<Object, Object> targetDataSources = new HashMap();
        targetDataSources.put("dataSource", this.defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        targetDataSources.putAll(this.customDataSources);
        for (String key : this.customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", this.defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        beanDefinitionRegistry.registerBeanDefinition("dataSource", beanDefinition);
    }
    
    public DataSource buildDataSource(Map<String, Object> dataSourceMap) {
        try {
            Object type = dataSourceMap.get("type");
            if (type == null) {
                type = "com.alibaba.druid.pool.DruidDataSource";
            }
            Class dataSourceType = Class.forName((String)type);
            String driverClassName = dataSourceMap.get("driver").toString();
            String url = dataSourceMap.get("url").toString();
            String username = dataSourceMap.get("username").toString();
            String password = dataSourceMap.get("password").toString();
            DataSourceBuilder factory = DataSourceBuilder.create().driverClassName(driverClassName).url(url).username(username).password(password).type(dataSourceType);
            return factory.build();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
