
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
        this.observers = new HashMap<EventType, Set<Object>>();
        this.eventQueue = new LinkedBlockingQueue<BaseGameEvent>();
        new NamedThreadFactory("event-dispatch").newThread(new EventWorker()).start();
    }
    
    public static EventDispatcher getInstance() {
        return EventDispatcher.instance;
    }
    
    public void registerEvent(final EventType evtType, final Object listener) {
        Set<Object> listeners = this.observers.get(evtType);
        if (listeners == null) {
            listeners = new HashSet<Object>();
            this.observers.put(evtType, listeners);
        }
        listeners.add(listener);
    }
    
    public void fireEvent(final BaseGameEvent event) {
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
    
    private void triggerEvent(final BaseGameEvent event) {
        final EventType evtType = event.getEventType();
        final Set<Object> listeners = this.observers.get(evtType);
        if (listeners != null) {
            listeners.forEach(listener -> {
                try {
                    ListenerManager.INSTANCE.fireEvent(listener, event);
                }
                catch (final Exception e) {
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
                    while (true) {
                        final BaseGameEvent event = EventDispatcher.this.eventQueue.take();
                        EventDispatcher.this.triggerEvent(event);
                    }
                }
                catch (final Exception e) {
                    LoggerUtils.error("EventWorker run failed", e);
                    continue;
                }
            }
        }
    }
}
