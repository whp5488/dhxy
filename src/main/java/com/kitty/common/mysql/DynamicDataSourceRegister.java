
package com.kitty.common.mysql;

import org.slf4j.LoggerFactory;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.core.env.PropertyResolver;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.core.env.Environment;
import java.sql.SQLException;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import java.util.Collection;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.type.AnnotationMetadata;
import java.util.HashMap;
import org.springframework.core.convert.support.DefaultConversionService;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.PropertyValues;
import org.springframework.core.convert.ConversionService;
import org.slf4j.Logger;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware
{
    private static final Logger logger;
    private ConversionService conversionService;
    private PropertyValues dataSourcePropertyValues;
    private static final Object DATASOURCE_TYPE_DEFAULT;
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources;
    
    public DynamicDataSourceRegister() {
        this.conversionService = new DefaultConversionService();
        this.customDataSources = new HashMap<>();
    }
    
    public void registerBeanDefinitions(final AnnotationMetadata importingClassMetadata, final BeanDefinitionRegistry registry) {
        final Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        targetDataSources.put("dataSource", this.defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        targetDataSources.putAll(this.customDataSources);
        DynamicDataSourceContextHolder.dataSourceIds.addAll(this.customDataSources.keySet());
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        final MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", this.defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);
        DynamicDataSourceRegister.logger.info("Dynamic DataSource Registry");
    }
    
    public DataSource buildDataSource(final Map<String, Object> dsMap) {
        final String driverClassName = dsMap.get("driver-class-name").toString();
        final String url = dsMap.get("url").toString();
        final String username = dsMap.get("username").toString();
        final String password = dsMap.get("password").toString();
        final int initialSize = Integer.parseInt(dsMap.get("druid.initial-size").toString());
        final int maxActive = Integer.parseInt(dsMap.get("druid.max-active").toString());
        final int maxWait = Integer.parseInt(dsMap.get("druid.max-wait").toString());
        final boolean UseUnfairLock = "true".equals(dsMap.get("druid.use-unfair-lock").toString());
        final DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(driverClassName);
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMaxWait(maxWait);
        druidDataSource.setUseUnfairLock(UseUnfairLock);
        druidDataSource.setPoolPreparedStatements(false);
        try {
            druidDataSource.setFilters("stat, wall");
        }
        catch (final SQLException e) {
            e.printStackTrace();
        }
        return (DataSource)druidDataSource;
    }
    
    public void setEnvironment(final Environment env) {
        this.initDefaultDataSource(env);
        this.initCustomDataSources(env);
    }
    
    private void initDefaultDataSource(final Environment env) {
        final RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
        final Map<String, Object> dsMap = new HashMap<String, Object>();
        dsMap.put("type", propertyResolver.getProperty("type"));
        dsMap.put("driver-class-name", propertyResolver.getProperty("driver-class-name"));
        dsMap.put("url", propertyResolver.getProperty("url"));
        dsMap.put("username", propertyResolver.getProperty("username"));
        dsMap.put("password", propertyResolver.getProperty("password"));
        dsMap.put("druid.initial-size", propertyResolver.getProperty("druid.initial-size"));
        dsMap.put("druid.max-active", propertyResolver.getProperty("druid.max-active"));
        dsMap.put("druid.max-wait", propertyResolver.getProperty("druid.max-wait"));
        dsMap.put("druid.use-unfair-lock", propertyResolver.getProperty("druid.use-unfair-lock"));
        dsMap.put("druid.pool-prepared-statements", propertyResolver.getProperty("druid.pool-prepared-statements"));
        this.dataBinder(this.defaultDataSource = this.buildDataSource(dsMap), env);
    }
    
    private void dataBinder(final DataSource dataSource, final Environment env) {
        final RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
        dataBinder.setConversionService(this.conversionService);
        dataBinder.setIgnoreNestedProperties(false);
        dataBinder.setIgnoreInvalidFields(false);
        dataBinder.setIgnoreUnknownFields(true);
        if (this.dataSourcePropertyValues == null) {
            final Map<String, Object> rpr = new RelaxedPropertyResolver(env, "spring.datasource").getSubProperties(".");
            final Map<String, Object> values = new HashMap<String, Object>(rpr);
            values.remove("type");
            values.remove("driver-class-name");
            values.remove("url");
            values.remove("username");
            values.remove("password");
            this.dataSourcePropertyValues = new MutablePropertyValues(values);
        }
        dataBinder.bind(this.dataSourcePropertyValues);
    }
    
    private void initCustomDataSources(final Environment env) {
        final RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(env, "custom.datasource.");
        final RelaxedPropertyResolver propertyResolver2 = new RelaxedPropertyResolver(env, "spring.datasource.");
        final String dsPrefixs = propertyResolver.getProperty("names");
        for (final String dsPrefix : dsPrefixs.split(",")) {
            final Map<String, Object> dsMap = new HashMap<String, Object>();
            dsMap.put("type", propertyResolver2.getProperty("type"));
            dsMap.put("driver-class-name", propertyResolver.getProperty(dsPrefix + ".driver-class-name"));
            dsMap.put("url", propertyResolver.getProperty(dsPrefix + ".url"));
            dsMap.put("username", propertyResolver.getProperty(dsPrefix + ".username"));
            dsMap.put("password", propertyResolver.getProperty(dsPrefix + ".password"));
            dsMap.put("druid.initial-size", propertyResolver2.getProperty("druid.initial-size"));
            dsMap.put("druid.max-active", propertyResolver2.getProperty("druid.max-active"));
            dsMap.put("druid.max-wait", propertyResolver2.getProperty("druid.max-wait"));
            dsMap.put("druid.use-unfair-lock", propertyResolver2.getProperty("druid.use-unfair-lock"));
            dsMap.put("druid.pool-prepared-statements", propertyResolver2.getProperty("druid.pool-prepared-statements"));
            final DataSource ds = this.buildDataSource(dsMap);
            this.customDataSources.put(dsPrefix, ds);
            this.dataBinder(ds, env);
        }
    }
    
    static {
        logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
        DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
    }
}
