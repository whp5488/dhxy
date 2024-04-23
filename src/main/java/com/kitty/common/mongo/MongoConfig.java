package com.kitty.common.mongo;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoTypeMapper;

@Configuration
public class MongoConfig implements InitializingBean {
  @Autowired
  private MappingMongoConverter mappingConverter;
  
  public void afterPropertiesSet() throws Exception {
    this.mappingConverter.setTypeMapper((MongoTypeMapper)new DefaultMongoTypeMapper(null));
  }
}
