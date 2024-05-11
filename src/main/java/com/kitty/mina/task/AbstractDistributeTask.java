
package com.kitty.mina.task;

public abstract class AbstractDistributeTask implements DistributeTask
{
    protected int distributeKey;
    private long startMillis;
    private long endMillis;
    
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
    
    @Override
    public int distributeKey() {
        return this.distributeKey;
    }
    
    public long getStartMillis() {
        return this.startMillis;
    }
    
    public void markStartMillis() {
        this.startMillis = System.currentTimeMillis();
    }
    
    public long getEndMillis() {
        return this.endMillis;
    }
    
    public void markEndMillis() {
        this.endMillis = System.currentTimeMillis();
    }
}
