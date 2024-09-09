package com.kitty.system.game.party.model;

public class PartyDeny
{
    private int endTime;
    
    public int getEndTime() {
        return this.endTime;
    }
    
    public void setEndTime(final int endTime) {
        this.endTime = endTime;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyDeny)) {
            return false;
        }
        PartyDeny other = (PartyDeny)o;
        return other.canEqual(this) && this.getEndTime() == other.getEndTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyDeny;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getEndTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyDeny(endTime=" + this.getEndTime() + ")";
    }
    
    public PartyDeny() {
    }
    
    public PartyDeny(final int endTime) {
        this.endTime = endTime;
    }
}
