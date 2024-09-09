package com.kitty.system.game.activity.model.user.lu;

import java.io.Serializable;

public class WaterEventSet implements Serializable
{
    private byte round;
    private byte max;
    private int season;
    
    public byte getRound() {
        return this.round;
    }
    
    public byte getMax() {
        return this.max;
    }
    
    public int getSeason() {
        return this.season;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    public void setMax(final byte max) {
        this.max = max;
    }
    
    public void setSeason(final int season) {
        this.season = season;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WaterEventSet)) {
            return false;
        }
        WaterEventSet other = (WaterEventSet)o;
        return other.canEqual(this) && this.getRound() == other.getRound() && this.getMax() == other.getMax() && this.getSeason() == other.getSeason();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof WaterEventSet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getMax();
        result = result * 59 + this.getSeason();
        return result;
    }
    
    @Override
    public String toString() {
        return "WaterEventSet(round=" + (int)this.getRound() + ", max=" + (int)this.getMax() + ", season=" + this.getSeason() + ")";
    }
    
    public WaterEventSet(final byte round, final byte max, final int season) {
        this.round = 1;
        this.max = 3;
        this.season = 1;
        this.round = round;
        this.max = max;
        this.season = season;
    }
    
    public WaterEventSet() {
        this.round = 1;
        this.max = 3;
        this.season = 1;
    }
}
