package com.kitty.listener;

import com.kitty.common.thread.NamedThreadFactory;
import com.kitty.logs.LoggerUtils;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class EventDispatcher {
  private static EventDispatcher instance = new EventDispatcher();
  
  private final Map<EventType, Set<Object>> observers;
  
  private LinkedBlockingQueue<BaseGameEvent> eventQueue;
  
  private EventDispatcher() {
    this.observers = new HashMap<>();
    this.eventQueue = new LinkedBlockingQueue<>();
    (new NamedThreadFactory("event-dispatch")).newThread(new EventWorker()).start();
  }
  
  public static EventDispatcher getInstance() {
    return instance;
  }
  
  public void registerEvent(EventType evtType, Object listener) {
    Set<Object> listeners = this.observers.get(evtType);
    if (listeners == null) {
      listeners = new HashSet();
      this.observers.put(evtType, listeners);
    } 
    listeners.add(listener);
  }
  
  public void fireEvent(BaseGameEvent event) {
    if (event == null)
      throw new NullPointerException("event cannot be null"); 
    if (event.isSynchronized()) {
      triggerEvent(event);
    } else {
      this.eventQueue.add(event);
    } 
  }
  
  private void triggerEvent(BaseGameEvent event) {
    EventType evtType = event.getEventType();
    Set<Object> listeners = this.observers.get(evtType);
    if (listeners != null)
      listeners.forEach(listener -> {
            try {
              ListenerManager.INSTANCE.fireEvent(listener, event);
            } catch (Exception e) {
              LoggerUtils.error("triggerEvent failed", e);
            } 
          }); 
  }
  
  private class EventWorker implements Runnable {
    private EventWorker() {}
    
    public void run() {
      while (true) {
        try {
          while (true) {
            BaseGameEvent event = EventDispatcher.this.eventQueue.take();
            EventDispatcher.this.triggerEvent(event);
          }
        } catch (Exception e) {
          LoggerUtils.error("EventWorker run failed", e);
        } 
      } 
    }
  }
}
