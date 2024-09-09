package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_common_award")
public class SysCommonAward implements Serializable
{
    @Column("dict")
    private String dict;
    @Column("award")
    @Default("[]")
    private List<Param> award;
    @Column("special")
    private List<Param> special;
    
    public SysCommonAward() {
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public List<Param> getAward() {
        return this.award;
    }
    
    public List<Param> getSpecial() {
        return this.special;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setAward(final List<Param> award) {
        this.award = award;
    }
    
    public void setSpecial(final List<Param> special) {
        this.special = special;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysCommonAward)) {
            return false;
        }
        SysCommonAward other = (SysCommonAward)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        Object this$award = this.getAward();
        Object other$award = other.getAward();
        if (!((this$award != null) ? this$award.equals(other$award) : (other$award == null))) {
            return false;
        }
        Object this$special = this.getSpecial();
        Object other$special = other.getSpecial();
        return (this$special != null) ? this$special.equals(other$special) : (other$special == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysCommonAward;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        Object $special = this.getSpecial();
        result = result * 59 + (($special == null) ? 43 : $special.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysCommonAward(dict=" + this.getDict() + ", award=" + this.getAward() + ", special=" + this.getSpecial() + ")";
    }
}
