
package com.kitty.system.game.task.domain.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class RankHallQuestion implements Serializable
{
    private long i;
    private String n;
    private int t;
    
    public long getI() {
        return this.i;
    }
    
    public String getN() {
        return this.n;
    }
    
    public int getT() {
        return this.t;
    }
    
    public void setI(final long i) {
        this.i = i;
    }
    
    public void setN(final String n) {
        this.n = n;
    }
    
    public void setT(final int t) {
        this.t = t;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RankHallQuestion)) {
            return false;
        }
        final RankHallQuestion other = (RankHallQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getI() != other.getI()) {
            return false;
        }
        final Object this$n = this.getN();
        final Object other$n = other.getN();
        if (this$n == null) {
            if (other$n == null) {
                return this.getT() == other.getT();
            }
        }
        else if (this$n.equals(other$n)) {
            return this.getT() == other.getT();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RankHallQuestion;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $i = this.getI();
        result = result * 59 + (int)($i >>> 32 ^ $i);
        final Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        result = result * 59 + this.getT();
        return result;
    }
    
    @Override
    public String toString() {
        return "RankHallQuestion(i=" + this.getI() + ", n=" + this.getN() + ", t=" + this.getT() + ")";
    }
    
    @ConstructorProperties({ "i", "n", "t" })
    public RankHallQuestion(final long i, final String n, final int t) {
        this.i = i;
        this.n = n;
        this.t = t;
    }
    
    public RankHallQuestion() {
    }
}
