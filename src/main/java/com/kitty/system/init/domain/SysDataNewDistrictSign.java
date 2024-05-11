
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Comment;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_new_district_sign")
public class SysDataNewDistrictSign
{
    @Column("day")
    private byte day;
    @Comment("奖励")
    @Column("content")
    @Default("[]")
    private List<Param> list;
    
    public byte getDay() {
        return this.day;
    }
    
    public List<Param> getList() {
        return this.list;
    }
    
    public void setDay(final byte day) {
        this.day = day;
    }
    
    public void setList(final List<Param> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataNewDistrictSign)) {
            return false;
        }
        final SysDataNewDistrictSign other = (SysDataNewDistrictSign)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getDay() != other.getDay()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataNewDistrictSign;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getDay();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataNewDistrictSign(day=" + this.getDay() + ", list=" + this.getList() + ")";
    }
}
