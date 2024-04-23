package com.kitty.listener;

import com.kitty.listener.annotation.EventHandler;
import com.kitty.logs.LoggerUtils;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.springframework.util.ReflectionUtils;

public enum ListenerManager {
  INSTANCE;
  
  private Map<String, Method> map;
  
  ListenerManager() {
    this.map = new HashMap<>();
  }
  
  public void registerEventHandler(Object handler) {
    Method[] methods = handler.getClass().getDeclaredMethods();
    for (Method method : methods) {
      EventHandler mapperAnnotation = method.<EventHandler>getAnnotation(EventHandler.class);
      if (mapperAnnotation != null) {
        EventType[] eventTypes = mapperAnnotation.value();
        for (EventType eventType : eventTypes) {
          EventDispatcher.getInstance().registerEvent(eventType, handler);
          this.map.put(getKey(handler, eventType), method);
        } 
      } 
    } 
  }
  
  public void fireEvent(Object handler, BaseGameEvent event) {
    try {
      Method method = this.map.get(getKey(handler, event.getEventType()));
      ReflectionUtils.makeAccessible(method);
      ReflectionUtils.invokeMethod(method, handler, new Object[] { event });
    } catch (Exception e) {
      LoggerUtils.error("监听器执行异常,handler[{}] event[{}] error{}", new Object[] { handler
            .getClass().getSimpleName(), event.getEventType(), e });
    } 
  }
  
  private String getKey(Object handler, EventType eventType) {
    return handler.getClass().getName() + "-" + eventType.toString();
  }
}
