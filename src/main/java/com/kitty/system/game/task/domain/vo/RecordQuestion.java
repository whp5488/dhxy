
package com.kitty.system.game.task.domain.vo;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class RecordQuestion implements Serializable
{
    private int s;
    private List<Byte> r;
    private int q;
    private int e;
    private byte t;
    private int i;
    
    public RecordQuestion() {
        this.s = 0;
        this.r = new ArrayList<Byte>();
        this.q = 0;
        this.e = 0;
        this.t = 0;
        this.i = 0;
    }
    
    public int getS() {
        return this.s;
    }
    
    public List<Byte> getR() {
        return this.r;
    }
    
    public int getQ() {
        return this.q;
    }
    
    public int getE() {
        return this.e;
    }
    
    public byte getT() {
        return this.t;
    }
    
    public int getI() {
        return this.i;
    }
    
    public void setS(final int s) {
        this.s = s;
    }
    
    public void setR(final List<Byte> r) {
        this.r = r;
    }
    
    public void setQ(final int q) {
        this.q = q;
    }
    
    public void setE(final int e) {
        this.e = e;
    }
    
    public void setT(final byte t) {
        this.t = t;
    }
    
    public void setI(final int i) {
        this.i = i;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RecordQuestion)) {
            return false;
        }
        final RecordQuestion other = (RecordQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getS() != other.getS()) {
            return false;
        }
        final Object this$r = this.getR();
        final Object other$r = other.getR();
        if (this$r == null) {
            if (other$r == null) {
                return this.getQ() == other.getQ() && this.getE() == other.getE() && this.getT() == other.getT() && this.getI() == other.getI();
            }
        }
        else if (this$r.equals(other$r)) {
            return this.getQ() == other.getQ() && this.getE() == other.getE() && this.getT() == other.getT() && this.getI() == other.getI();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RecordQuestion;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getS();
        final Object $r = this.getR();
        result = result * 59 + (($r == null) ? 43 : $r.hashCode());
        result = result * 59 + this.getQ();
        result = result * 59 + this.getE();
        result = result * 59 + this.getT();
        result = result * 59 + this.getI();
        return result;
    }
    
    @Override
    public String toString() {
        return "RecordQuestion(s=" + this.getS() + ", r=" + this.getR() + ", q=" + this.getQ() + ", e=" + this.getE() + ", t=" + this.getT() + ", i=" + this.getI() + ")";
    }
}
