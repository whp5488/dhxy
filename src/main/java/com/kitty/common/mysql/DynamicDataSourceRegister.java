package com.kitty.common.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.bind.RelaxedDataBinder;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotationMetadata;

public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {
  private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);
  
  private ConversionService conversionService = (ConversionService)new DefaultConversionService();
  
  private PropertyValues dataSourcePropertyValues;
  
  private static final Object DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";
  
  private DataSource defaultDataSource;
  
  private Map<String, DataSource> customDataSources = new HashMap<>();
  
  public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
    Map<Object, Object> targetDataSources = new HashMap<>();
    targetDataSources.put("dataSource", this.defaultDataSource);
    DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
    targetDataSources.putAll(this.customDataSources);
    DynamicDataSourceContextHolder.dataSourceIds.addAll(this.customDataSources.keySet());
    GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
    beanDefinition.setBeanClass(DynamicDataSource.class);
    beanDefinition.setSynthetic(true);
    MutablePropertyValues mpv = beanDefinition.getPropertyValues();
    mpv.addPropertyValue("defaultTargetDataSource", this.defaultDataSource);
    mpv.addPropertyValue("targetDataSources", targetDataSources);
    registry.registerBeanDefinition("dataSource", (BeanDefinition)beanDefinition);
    logger.info("Dynamic DataSource Registry");
  }
  
  public DataSource buildDataSource(Map<String, Object> dsMap) {
    String driverClassName = dsMap.get("driver-class-name").toString();
    String url = dsMap.get("url").toString();
    String username = dsMap.get("username").toString();
    String password = dsMap.get("password").toString();
    int initialSize = Integer.parseInt(dsMap.get("druid.initial-size").toString());
    int maxActive = Integer.parseInt(dsMap.get("druid.max-active").toString());
    int maxWait = Integer.parseInt(dsMap.get("druid.max-wait").toString());
    boolean UseUnfairLock = "true".equals(dsMap.get("druid.use-unfair-lock").toString());
    DruidDataSource druidDataSource = new DruidDataSource();
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
    } catch (SQLException e) {
      e.printStackTrace();
    } 
    return (DataSource)druidDataSource;
  }
  
  public void setEnvironment(Environment env) {
    initDefaultDataSource(env);
    initCustomDataSources(env);
  }
  
  private void initDefaultDataSource(Environment env) {
    RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver((PropertyResolver)env, "spring.datasource.");
    Map<String, Object> dsMap = new HashMap<>();
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
    this.defaultDataSource = buildDataSource(dsMap);
    dataBinder(this.defaultDataSource, env);
  }
  
  private void dataBinder(DataSource dataSource, Environment env) {
    RelaxedDataBinder dataBinder = new RelaxedDataBinder(dataSource);
    dataBinder.setConversionService(this.conversionService);
    dataBinder.setIgnoreNestedProperties(false);
    dataBinder.setIgnoreInvalidFields(false);
    dataBinder.setIgnoreUnknownFields(true);
    if (this.dataSourcePropertyValues == null) {
      Map<String, Object> rpr = (new RelaxedPropertyResolver((PropertyResolver)env, "spring.datasource")).getSubProperties(".");
      Map<String, Object> values = new HashMap<>(rpr);
      values.remove("type");
      values.remove("driver-class-name");
      values.remove("url");
      values.remove("username");
      values.remove("password");
      this.dataSourcePropertyValues = (PropertyValues)new MutablePropertyValues(values);
    } 
    dataBinder.bind(this.dataSourcePropertyValues);
  }
  
  private void initCustomDataSources(Environment env) {
    RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver((PropertyResolver)env, "custom.datasource.");
    RelaxedPropertyResolver propertyResolver2222 = new RelaxedPropertyResolver((PropertyResolver)env, "spring.datasource.");
    String dsPrefixs = propertyResolver.getProperty("names");
    for (String dsPrefix : dsPrefixs.split(",")) {
      Map<String, Object> dsMap = new HashMap<>();
      dsMap.put("type", propertyResolver2222.getProperty("type"));
      dsMap.put("driver-class-name", propertyResolver.getProperty(dsPrefix + ".driver-class-name"));
      dsMap.put("url", propertyResolver.getProperty(dsPrefix + ".url"));
      dsMap.put("username", propertyResolver.getProperty(dsPrefix + ".username"));
      dsMap.put("password", propertyResolver.getProperty(dsPrefix + ".password"));
      dsMap.put("druid.initial-size", propertyResolver2222.getProperty("druid.initial-size"));
      dsMap.put("druid.max-active", propertyResolver2222.getProperty("druid.max-active"));
      dsMap.put("druid.max-wait", propertyResolver2222.getProperty("druid.max-wait"));
      dsMap.put("druid.use-unfair-lock", propertyResolver2222.getProperty("druid.use-unfair-lock"));
      dsMap.put("druid.pool-prepared-statements", propertyResolver2222.getProperty("druid.pool-prepared-statements"));
      DataSource ds = buildDataSource(dsMap);
      this.customDataSources.put(dsPrefix, ds);
      dataBinder(ds, env);
    } 
  }
}
