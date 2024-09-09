package com.kitty.system.init.domain.vo;

import java.util.ArrayList;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import java.util.List;

public class MindDefineRange
{
    private int s;
    private int t;
    private List<Double> range;
    
    public MindDefineRange(int s, List<Double> range) {
        this.t = PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue();
        this.range = new ArrayList<>();
        this.s = s;
        this.range = range;
    }
    
    public int getS() {
        return this.s;
    }
    
    public int getT() {
        return this.t;
    }
    
    public List<Double> getRange() {
        return this.range;
    }
    
    public void setS(final int s) {
        this.s = s;
    }
    
    public void setT(final int t) {
        this.t = t;
    }
    
    public void setRange(final List<Double> range) {
        this.range = range;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MindDefineRange)) {
            return false;
        }
        MindDefineRange other = (MindDefineRange)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getS() != other.getS()) {
            return false;
        }
        if (this.getT() != other.getT()) {
            return false;
        }
        Object this$range = this.getRange();
        Object other$range = other.getRange();
        return (this$range != null) ? this$range.equals(other$range) : (other$range == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MindDefineRange;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getS();
        result = result * 59 + this.getT();
        Object $range = this.getRange();
        result = result * 59 + (($range == null) ? 43 : $range.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MindDefineRange(s=" + this.getS() + ", t=" + this.getT() + ", range=" + this.getRange() + ")";
    }
    
    public MindDefineRange(final int s, final int t, final List<Double> range) {
        this.t = PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue();
        this.range = new ArrayList<>();
        this.s = s;
        this.t = t;
        this.range = range;
    }
    
    public MindDefineRange() {
        this.t = PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue();
        this.range = new ArrayList<>();
    }
}
