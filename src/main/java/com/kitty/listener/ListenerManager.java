
package com.kitty.listener;

import com.kitty.logs.LoggerUtils;
import org.springframework.util.ReflectionUtils;
import com.kitty.listener.annotation.EventHandler;
import java.util.HashMap;
import java.lang.reflect.Method;
import java.util.Map;

public enum ListenerManager
{
    INSTANCE;
    
    private Map<String, Method> map;
    
    private ListenerManager() {
        this.map = new HashMap<String, Method>();
    }
    
    public void registerEventHandler(final Object handler) {
        final Method[] declaredMethods;
        final Method[] methods = declaredMethods = handler.getClass().getDeclaredMethods();
        for (final Method method : declaredMethods) {
            final EventHandler mapperAnnotation = method.getAnnotation(EventHandler.class);
            if (mapperAnnotation != null) {
                final EventType[] value;
                final EventType[] eventTypes = value = mapperAnnotation.value();
                for (final EventType eventType : value) {
                    EventDispatcher.getInstance().registerEvent(eventType, handler);
                    this.map.put(this.getKey(handler, eventType), method);
                }
            }
        }
    }
    
    public void fireEvent(final Object handler, final BaseGameEvent event) {
        try {
            final Method method = this.map.get(this.getKey(handler, event.getEventType()));
            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, handler, new Object[] { event });
        }
        catch (final Exception e) {
            LoggerUtils.error("监听器执行异常,handler[{}] event[{}] error{}", handler.getClass().getSimpleName(), event.getEventType(), e);
        }
    }
    
    private String getKey(final Object handler, final EventType eventType) {
        return handler.getClass().getName() + "-" + eventType.toString();
    }
}
