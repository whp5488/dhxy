package com.kitty.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new MyInterceptor())
      .addPathPatterns(new String[] { "/*" });
  }
  
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**").allowedHeaders(new String[] { "*" }).allowedMethods(new String[] { "*" }).allowedOrigins(new String[] { "*" }).allowCredentials(true);
  }
}
