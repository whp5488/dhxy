
package com.kitty.mina;

public class FloodRecord
{
    private int receivedPacksLastSecond;
    private long lastReceivedTime;
    private int floodTimes;
    private long lastFloodTime;
    
    public int getReceivedPacksLastSecond() {
        return this.receivedPacksLastSecond;
    }
    
    public void setReceivedPacksLastSecond(final int receivedPacksLastSecond) {
        this.receivedPacksLastSecond = receivedPacksLastSecond;
    }
    
    public int addSecondReceivedPackage() {
        return ++this.receivedPacksLastSecond;
    }
    
    public long getLastReceivedTime() {
        return this.lastReceivedTime;
    }
    
    public void setLastReceivedTime(final long lastReceivedTime) {
        this.lastReceivedTime = lastReceivedTime;
    }
    
    public int getFloodTimes() {
        return this.floodTimes;
    }
    
    public void setFloodTimes(final int floodTimes) {
        this.floodTimes = floodTimes;
    }
    
    public int addFloodTimes() {
        return ++this.floodTimes;
    }
    
    public long getLastFloodTime() {
        return this.lastFloodTime;
    }
    
    public void setLastFloodTime(final long lastFloodTime) {
        this.lastFloodTime = lastFloodTime;
    }
}
