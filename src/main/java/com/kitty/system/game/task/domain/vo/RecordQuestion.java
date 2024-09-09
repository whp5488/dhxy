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
        this.r = new ArrayList<>();
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
        RecordQuestion other = (RecordQuestion)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getS() != other.getS()) {
            return false;
        }
        Object this$r = this.getR();
        Object other$r = other.getR();
        return ((this$r != null) ? this$r.equals(other$r) : (other$r == null)) && this.getQ() == other.getQ() && this.getE() == other.getE() && this.getT() == other.getT() && this.getI() == other.getI();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RecordQuestion;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getS();
        Object $r = this.getR();
        result = result * 59 + (($r == null) ? 43 : $r.hashCode());
        result = result * 59 + this.getQ();
        result = result * 59 + this.getE();
        result = result * 59 + this.getT();
        result = result * 59 + this.getI();
        return result;
    }
    
    @Override
    public String toString() {
        return "RecordQuestion(s=" + this.getS() + ", r=" + this.getR() + ", q=" + this.getQ() + ", e=" + this.getE() + ", t=" + (int)this.getT() + ", i=" + this.getI() + ")";
    }
}
