package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_above_attr_lib")
public class SysAboveAttrLib implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Column("dict")
    private String dict;
    @Column("base_attr")
    @ColDefine(customType = "text")
    private String baseAttr;
    @Column("rnd_range")
    private String rndRange;
    
    public SysAboveAttrLib() {
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public String getBaseAttr() {
        return this.baseAttr;
    }
    
    public String getRndRange() {
        return this.rndRange;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setBaseAttr(final String baseAttr) {
        this.baseAttr = baseAttr;
    }
    
    public void setRndRange(final String rndRange) {
        this.rndRange = rndRange;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysAboveAttrLib)) {
            return false;
        }
        SysAboveAttrLib other = (SysAboveAttrLib)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        Object this$baseAttr = this.getBaseAttr();
        Object other$baseAttr = other.getBaseAttr();
        if (!((this$baseAttr != null) ? this$baseAttr.equals(other$baseAttr) : (other$baseAttr == null))) {
            return false;
        }
        Object this$rndRange = this.getRndRange();
        Object other$rndRange = other.getRndRange();
        return (this$rndRange != null) ? this$rndRange.equals(other$rndRange) : (other$rndRange == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysAboveAttrLib;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        Object $rndRange = this.getRndRange();
        result = result * 59 + (($rndRange == null) ? 43 : $rndRange.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysAboveAttrLib(dict=" + this.getDict() + ", baseAttr=" + this.getBaseAttr() + ", rndRange=" + this.getRndRange() + ")";
    }
}
