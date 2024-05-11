
package com.kitty.system.game.ranking.domain;

import java.beans.ConstructorProperties;
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
        final RankEquip other = (RankEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getI() != other.getI()) {
            return false;
        }
        if (this.getS() != other.getS()) {
            return false;
        }
        final Object this$n = this.getN();
        final Object other$n = other.getN();
        if (this$n == null) {
            if (other$n == null) {
                return true;
            }
        }
        else if (this$n.equals(other$n)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RankEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $i = this.getI();
        result = result * 59 + (int)($i >>> 32 ^ $i);
        result = result * 59 + this.getS();
        final Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RankEquip(i=" + this.getI() + ", s=" + this.getS() + ", n=" + this.getN() + ")";
    }
    
    @ConstructorProperties({ "i", "s", "n" })
    public RankEquip(final long i, final int s, final String n) {
        this.i = i;
        this.s = s;
        this.n = n;
    }
    
    public RankEquip() {
    }
}
