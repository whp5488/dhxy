package com.kitty.system.game.ranking.domain;

import java.io.Serializable;

public class RankEquip implements Serializable
{
    private long i;
    private int s;
    private String n;
    
    public long getI() {
        return this.i;
    }
    
    public int getS() {
        return this.s;
    }
    
    public String getN() {
        return this.n;
    }
    
    public void setI(final long i) {
        this.i = i;
    }
    
    public void setS(final int s) {
        this.s = s;
    }
    
    public void setN(final String n) {
        this.n = n;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RankEquip)) {
            return false;
        }
        RankEquip other = (RankEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getI() != other.getI()) {
            return false;
        }
        if (this.getS() != other.getS()) {
            return false;
        }
        Object this$n = this.getN();
        Object other$n = other.getN();
        return (this$n != null) ? this$n.equals(other$n) : (other$n == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RankEquip;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $i = this.getI();
        result = result * 59 + (int)($i >>> 32 ^ $i);
        result = result * 59 + this.getS();
        Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RankEquip(i=" + this.getI() + ", s=" + this.getS() + ", n=" + this.getN() + ")";
    }
    
    public RankEquip(final long i, final int s, final String n) {
        this.i = i;
        this.s = s;
        this.n = n;
    }
    
    public RankEquip() {
    }
}
