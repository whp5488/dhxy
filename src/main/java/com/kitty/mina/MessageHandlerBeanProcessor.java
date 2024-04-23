package com.kitty.mina;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Component
public class MessageHandlerBeanProcessor implements BeanPostProcessor, ApplicationContextAware, Ordered {
  public Object postProcessBeforeInitialization(Object o, String s) throws BeansException {
    MessageDispatcher.getInstance().registerMessageHandler(o);
    return o;
  }
  
  public Object postProcessAfterInitialization(Object o, String s) throws BeansException {
    return o;
  }
  
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {}
  
  public int getOrder() {
    return Integer.MAX_VALUE;
  }
}
