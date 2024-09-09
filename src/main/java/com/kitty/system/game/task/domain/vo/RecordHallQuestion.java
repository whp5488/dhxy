package com.kitty.system.game.task.domain.vo;

import java.io.Serializable;

public class RecordHallQuestion implements Serializable
{
    private int s;
    private int q;
    private int t;
    private byte r;
    
    public RecordHallQuestion() {
        this.r = -1;
    }
    
    public int getS() {
        return this.s;
    }
    
    public int getQ() {
        return this.q;
    }
    
    public int getT() {
        return this.t;
    }
    
    public byte getR() {
        return this.r;
    }
    
    public void setS(final int s) {
        this.s = s;
    }
    
    public void setQ(final int q) {
        this.q = q;
    }
    
    public void setT(final int t) {
        this.t = t;
    }
    
    public void setR(final byte r) {
        this.r = r;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RecordHallQuestion)) {
            return false;
        }
        RecordHallQuestion other = (RecordHallQuestion)o;
        return other.canEqual(this) && this.getS() == other.getS() && this.getQ() == other.getQ() && this.getT() == other.getT() && this.getR() == other.getR();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RecordHallQuestion;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getS();
        result = result * 59 + this.getQ();
        result = result * 59 + this.getT();
        result = result * 59 + this.getR();
        return result;
    }
    
    @Override
    public String toString() {
        return "RecordHallQuestion(s=" + this.getS() + ", q=" + this.getQ() + ", t=" + this.getT() + ", r=" + (int)this.getR() + ")";
    }
}
