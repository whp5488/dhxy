package com.kitty.system.game.party.model.vo;

public class PartyTask
{
    private int time;
    private int count;
    
    public int getTime() {
        return this.time;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyTask)) {
            return false;
        }
        PartyTask other = (PartyTask)o;
        return other.canEqual(this) && this.getTime() == other.getTime() && this.getCount() == other.getCount();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyTask;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getTime();
        result = result * 59 + this.getCount();
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyTask(time=" + this.getTime() + ", count=" + this.getCount() + ")";
    }
    
    public PartyTask(final int time, final int count) {
        this.time = time;
        this.count = count;
    }
    
    public PartyTask() {
    }
}
