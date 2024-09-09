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
        this.map = new HashMap();
    }
    
    public void registerEventHandler(Object handler) {
        Method[] methods;
        for (Method method : methods = handler.getClass().getDeclaredMethods()) {
            EventHandler mapperAnnotation = (EventHandler)method.getAnnotation(EventHandler.class);
            if (mapperAnnotation != null) {
                EventType[] eventTypes;
                for (EventType eventType : eventTypes = mapperAnnotation.value()) {
                    EventDispatcher.getInstance().registerEvent(eventType, handler);
                    this.map.put(this.getKey(handler, eventType), method);
                }
            }
        }
    }
    
    public void fireEvent(Object handler, BaseGameEvent event) {
        try {
            Method method = (Method)this.map.get(this.getKey(handler, event.getEventType()));
            ReflectionUtils.makeAccessible(method);
            ReflectionUtils.invokeMethod(method, handler, new Object[] { event });
        }
        catch (Exception e) {
            LoggerUtils.error("监听器执行异常,handler[{}] event[{}] error{}", handler.getClass().getSimpleName(), event.getEventType(), e);
        }
    }
    
    private String getKey(Object handler, EventType eventType) {
        return handler.getClass().getName() + "-" + eventType.toString();
    }
}
