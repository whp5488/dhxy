
package com.kitty.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.serializer.RedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.CachingConfigurerSupport;

@Configuration
public class RedisConfig extends CachingConfigurerSupport
{
    @Value("${spring.redis.database}")
    private int database;
    
    @Bean({ "redisTemplate" })
    public RedisTemplate redisTemplate(final RedisConnectionFactory factory) {
        final StringRedisTemplate template = new StringRedisTemplate(factory);
        final Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        final ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer((RedisSerializer)jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return (RedisTemplate)template;
    }
}
