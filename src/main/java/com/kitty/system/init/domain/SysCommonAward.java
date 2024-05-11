
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
        final SysCommonAward other = (SysCommonAward)o;
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
        final Object this$award = this.getAward();
        final Object other$award = other.getAward();
        Label_0102: {
            if (this$award == null) {
                if (other$award == null) {
                    break Label_0102;
                }
            }
            else if (this$award.equals(other$award)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$special = this.getSpecial();
        final Object other$special = other.getSpecial();
        if (this$special == null) {
            if (other$special == null) {
                return true;
            }
        }
        else if (this$special.equals(other$special)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysCommonAward;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        final Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        final Object $special = this.getSpecial();
        result = result * 59 + (($special == null) ? 43 : $special.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysCommonAward(dict=" + this.getDict() + ", award=" + this.getAward() + ", special=" + this.getSpecial() + ")";
    }
}
