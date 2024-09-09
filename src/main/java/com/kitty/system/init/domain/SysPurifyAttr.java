package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_purify_attr")
public class SysPurifyAttr implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Column("dict")
    private String dict;
    @Column("lower_bound")
    private int lowerBound;
    @Column("upper_bound")
    private int upperBound;
    
    public SysPurifyAttr() {
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public int getLowerBound() {
        return this.lowerBound;
    }
    
    public int getUpperBound() {
        return this.upperBound;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setLowerBound(final int lowerBound) {
        this.lowerBound = lowerBound;
    }
    
    public void setUpperBound(final int upperBound) {
        this.upperBound = upperBound;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPurifyAttr)) {
            return false;
        }
        SysPurifyAttr other = (SysPurifyAttr)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        return ((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null)) && this.getLowerBound() == other.getLowerBound() && this.getUpperBound() == other.getUpperBound();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPurifyAttr;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        result = result * 59 + this.getLowerBound();
        result = result * 59 + this.getUpperBound();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPurifyAttr(dict=" + this.getDict() + ", lowerBound=" + this.getLowerBound() + ", upperBound=" + this.getUpperBound() + ")";
    }
}
