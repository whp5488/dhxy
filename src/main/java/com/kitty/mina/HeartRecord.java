package com.kitty.mina;

public class HeartRecord {
  private long lastTime;
  
  private long currTime;
  
  private int illegalCount;
  
  public void setLastTime(long lastTime) {
    this.lastTime = lastTime;
  }
  
  public void setCurrTime(long currTime) {
    this.currTime = currTime;
  }
  
  public void setIllegalCount(int illegalCount) {
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
  
  public boolean addCount() {
    this.illegalCount++;
    return (this.illegalCount >= 5);
  }
}
