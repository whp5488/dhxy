package com.kitty.system.game.task.domain.vo;

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
        RankHallQuestion other = (RankHallQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getI() != other.getI()) {
            return false;
        }
        Object this$n = this.getN();
        Object other$n = other.getN();
        return ((this$n != null) ? this$n.equals(other$n) : (other$n == null)) && this.getT() == other.getT();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RankHallQuestion;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $i = this.getI();
        result = result * 59 + (int)($i >>> 32 ^ $i);
        Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        result = result * 59 + this.getT();
        return result;
    }
    
    @Override
    public String toString() {
        return "RankHallQuestion(i=" + this.getI() + ", n=" + this.getN() + ", t=" + this.getT() + ")";
    }
    
    public RankHallQuestion(final long i, final String n, final int t) {
        this.i = i;
        this.n = n;
        this.t = t;
    }
    
    public RankHallQuestion() {
    }
}
