package com.kitty.listener;

public abstract class BaseGameEvent
{
    private long createTime;
    private final EventType eventType;
    
    public BaseGameEvent(EventType evtType) {
        this.createTime = System.currentTimeMillis();
        this.eventType = evtType;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public EventType getEventType() {
        return this.eventType;
    }
    
    public boolean isSynchronized() {
        return true;
    }
}
