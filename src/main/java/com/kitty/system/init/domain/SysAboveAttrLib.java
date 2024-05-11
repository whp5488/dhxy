
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
        final SysAboveAttrLib other = (SysAboveAttrLib)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$dict = this.getDict();
        final Object other$dict = other.getDict();
        Label_0065: {
            if (this$dict == null) {
                if (other$dict == null) {
                    break Label_0065;
                }
            }
            else if (this$dict.equals(other$dict)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$baseAttr = this.getBaseAttr();
        final Object other$baseAttr = other.getBaseAttr();
        Label_0102: {
            if (this$baseAttr == null) {
                if (other$baseAttr == null) {
                    break Label_0102;
                }
            }
            else if (this$baseAttr.equals(other$baseAttr)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$rndRange = this.getRndRange();
        final Object other$rndRange = other.getRndRange();
        if (this$rndRange == null) {
            if (other$rndRange == null) {
                return true;
            }
        }
        else if (this$rndRange.equals(other$rndRange)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysAboveAttrLib;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        final Object $baseAttr = this.getBaseAttr();
        result = result * 59 + (($baseAttr == null) ? 43 : $baseAttr.hashCode());
        final Object $rndRange = this.getRndRange();
        result = result * 59 + (($rndRange == null) ? 43 : $rndRange.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysAboveAttrLib(dict=" + this.getDict() + ", baseAttr=" + this.getBaseAttr() + ", rndRange=" + this.getRndRange() + ")";
    }
}
