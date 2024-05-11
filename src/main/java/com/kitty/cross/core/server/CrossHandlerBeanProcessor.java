
package com.kitty.cross.core.server;

import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;
import org.springframework.core.Ordered;
import org.springframework.context.ApplicationContextAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

@Component
public class CrossHandlerBeanProcessor implements BeanPostProcessor, ApplicationContextAware, Ordered
{
    public Object postProcessBeforeInitialization(final Object o, final String s) throws BeansException {
        BaseCMessageDispatcher.getInstance().registerEventHandler(o);
        return o;
    }
    
    public Object postProcessAfterInitialization(final Object o, final String s) throws BeansException {
        return o;
    }
    
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
    }
    
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
