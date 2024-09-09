package com.kitty.common.mongo;

import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.InitializingBean;

@Configuration
public class MongoConfig implements InitializingBean
{
    @Autowired
    private MappingMongoConverter mappingConverter;
    
    public MongoConfig() {
    }
    
    public void afterPropertiesSet() throws Exception {
        this.mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
    }
}
