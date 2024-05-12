
package com.kitty.support;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.ConversionService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupportConfiration
{
    @Bean({ "gameConversion" })
    public ConversionService createConversionService() {
        return new DefaultConversionService();
    }
}
