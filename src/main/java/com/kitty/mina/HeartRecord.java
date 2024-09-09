package com.kitty.mina;

public class HeartRecord
{
    private long lastTime;
    private long currTime;
    private int illegalCount;
    
    public HeartRecord() {
    }
    
    public boolean addCount() {
        ++this.illegalCount;
        return this.illegalCount >= 5;
    }
    
    public void setLastTime(final long lastTime) {
        this.lastTime = lastTime;
    }
    
    public void setCurrTime(final long currTime) {
        this.currTime = currTime;
    }
    
    public void setIllegalCount(final int illegalCount) {
        this.illegalCount = illegalCount;
    }
    
    public long getLastTime() {
        return this.lastTime;
    }
    
    public long getCurrTime() {
        return this.currTime;
    }
    
    public int getIllegalCount() {
        return this.illegalCount;
    }
}
