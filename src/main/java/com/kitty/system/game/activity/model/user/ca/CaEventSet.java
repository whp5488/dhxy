
package com.kitty.system.game.activity.model.user.ca;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class CaEventSet implements Serializable
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
        if (!(o instanceof CaEventSet)) {
            return false;
        }
        final CaEventSet other = (CaEventSet)o;
        return other.canEqual(this) && this.getRound() == other.getRound() && this.getMax() == other.getMax() && this.getSeason() == other.getSeason();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CaEventSet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getMax();
        result = result * 59 + this.getSeason();
        return result;
    }
    
    @Override
    public String toString() {
        return "CaEventSet(round=" + this.getRound() + ", max=" + this.getMax() + ", season=" + this.getSeason() + ")";
    }
    
    @ConstructorProperties({ "round", "max", "season" })
    public CaEventSet(final byte round, final byte max, final int season) {
        this.round = 1;
        this.max = 3;
        this.season = 1;
        this.round = round;
        this.max = max;
        this.season = season;
    }
    
    public CaEventSet() {
        this.round = 1;
        this.max = 3;
        this.season = 1;
    }
}
