package com.kitty.listener;

import com.kitty.logs.LoggerUtils;
import java.util.HashSet;
import com.kitty.common.thread.NamedThreadFactory;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.Set;
import java.util.Map;

public class EventDispatcher
{
    private static EventDispatcher instance;
    private final Map<EventType, Set<Object>> observers;
    private LinkedBlockingQueue<BaseGameEvent> eventQueue;
    
    private EventDispatcher() {
        this.observers = new HashMap();
        this.eventQueue = new LinkedBlockingQueue();
        new NamedThreadFactory("event-dispatch").newThread(new EventDispatcher.EventWorker()).start();
    }
    
    public static EventDispatcher getInstance() {
        return EventDispatcher.instance;
    }
    
    public void registerEvent(EventType evtType, Object listener) {
        Set<Object> listeners = (Set)this.observers.get(evtType);
        if (listeners == null) {
            listeners = new HashSet();
            this.observers.put(evtType, listeners);
        }
        listeners.add(listener);
    }
    
    public void fireEvent(BaseGameEvent event) {
        if (event == null) {
            throw new NullPointerException("event cannot be null");
        }
        if (event.isSynchronized()) {
            this.triggerEvent(event);
        }
        else {
            this.eventQueue.add(event);
        }
    }
    
    private void triggerEvent(BaseGameEvent event) {
        EventType evtType = event.getEventType();
        Set<Object> listeners = (Set)this.observers.get(evtType);
        if (listeners != null) {
            listeners.forEach(listener -> {
                try {
                    ListenerManager.INSTANCE.fireEvent(listener, event);
                }
                catch (Exception e) {
                    LoggerUtils.error("triggerEvent failed", e);
                }
            });
        }
    }
    
    static {
        EventDispatcher.instance = new EventDispatcher();
    }
    
    private class EventWorker implements Runnable
    {
        @Override
        public void run() {
            while (true) {
                try {
                    BaseGameEvent event = (BaseGameEvent)EventDispatcher.this.eventQueue.take();
                    EventDispatcher.this.triggerEvent(event);
                    continue;
                }
                catch (Exception e) {
                    LoggerUtils.error("EventWorker run failed", e);
                }
            }
        }
    }
}
