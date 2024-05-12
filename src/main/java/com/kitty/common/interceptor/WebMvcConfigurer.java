
package com.kitty.common.interceptor;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter
{
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor((HandlerInterceptor)new MyInterceptor()).addPathPatterns("/*");
    }
    
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**").allowedHeaders("*").allowedMethods("*").allowedOrigins("*").allowCredentials(true);
    }
}
