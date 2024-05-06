package com.kitty.common.start;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServerConfiguration {
  @Value("${server.port}")
  private int port;
  
  @Bean
  public EmbeddedServletContainerFactory createEmbeddedServletContainerFactory() {
    TomcatEmbeddedServletContainerFactory tomcatFactory = new TomcatEmbeddedServletContainerFactory();
    tomcatFactory.setPort(this.port);
    tomcatFactory.addConnectorCustomizers(new MyTomcatConnectorCustomizer());
    return tomcatFactory;
  }
}
