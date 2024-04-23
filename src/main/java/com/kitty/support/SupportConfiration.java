package com.kitty.support;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class SupportConfiration {
  @Bean({"gameConversion"})
  public ConversionService createConversionService() {
    return (ConversionService)new DefaultConversionService();
  }
}
