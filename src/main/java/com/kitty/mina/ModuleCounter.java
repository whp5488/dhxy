
package com.kitty.mina;

import com.kitty.common.utils.JsonUtils;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.io.Serializable;

public class ModuleCounter implements Serializable
{
    private boolean checked;
    private long loginTime;
    private long lastPackTime;
    private AtomicLong count;
    private Map<Integer, AtomicInteger> counterMap;
    
    public ModuleCounter() {
        this.count = new AtomicLong(0L);
        this.counterMap = new ConcurrentHashMap<Integer, AtomicInteger>();
    }
    
    public boolean isChecked() {
        return this.checked;
    }
    
    public void setChecked(final boolean checked) {
        this.checked = checked;
    }
    
    public long getLoginTime() {
        return this.loginTime;
    }
    
    public void setLoginTime(final long loginTime) {
        this.loginTime = loginTime;
    }
    
    public long getLastPackTime() {
        return this.lastPackTime;
    }
    
    public void setLastPackTime(final long lastPackTime) {
        this.lastPackTime = lastPackTime;
    }
    
    public Map<Integer, AtomicInteger> getCounterMap() {
        return this.counterMap;
    }
    
    public void setCounterMap(final Map<Integer, AtomicInteger> counterMap) {
        this.counterMap = counterMap;
    }
    
    public AtomicLong getCount() {
        return this.count;
    }
    
    public void setCount(final AtomicLong count) {
        this.count = count;
    }
    
    @Override
    public String toString() {
        return JsonUtils.object2String(this);
    }
}
