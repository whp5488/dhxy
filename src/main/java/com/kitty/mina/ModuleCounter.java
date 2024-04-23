package com.kitty.mina;

import com.kitty.common.utils.JsonUtils;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ModuleCounter implements Serializable {
  private boolean checked;
  
  private long loginTime;
  
  private long lastPackTime;
  
  private AtomicLong count = new AtomicLong(0L);
  
  private Map<Integer, AtomicInteger> counterMap = new ConcurrentHashMap<>();
  
  public boolean isChecked() {
    return this.checked;
  }
  
  public void setChecked(boolean checked) {
    this.checked = checked;
  }
  
  public long getLoginTime() {
    return this.loginTime;
  }
  
  public void setLoginTime(long loginTime) {
    this.loginTime = loginTime;
  }
  
  public long getLastPackTime() {
    return this.lastPackTime;
  }
  
  public void setLastPackTime(long lastPackTime) {
    this.lastPackTime = lastPackTime;
  }
  
  public Map<Integer, AtomicInteger> getCounterMap() {
    return this.counterMap;
  }
  
  public void setCounterMap(Map<Integer, AtomicInteger> counterMap) {
    this.counterMap = counterMap;
  }
  
  public AtomicLong getCount() {
    return this.count;
  }
  
  public void setCount(AtomicLong count) {
    this.count = count;
  }
  
  public String toString() {
    return JsonUtils.object2String(this);
  }
}
