
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_data_stall_menu")
public class SysDataStallMenu implements Serializable
{
    @Comment("菜单编号")
    @Column("serial")
    private Integer serial;
    @Comment("父菜单编号")
    @Column("parent_serial")
    private Integer parentSerial;
    @Comment("祖级")
    @Column("ancestors")
    private Integer ancestors;
    @Comment("0.正常，1.停用")
    @Column("status")
    private Integer categoryName;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getParentSerial() {
        return this.parentSerial;
    }
    
    public Integer getAncestors() {
        return this.ancestors;
    }
    
    public Integer getCategoryName() {
        return this.categoryName;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setParentSerial(final Integer parentSerial) {
        this.parentSerial = parentSerial;
    }
    
    public void setAncestors(final Integer ancestors) {
        this.ancestors = ancestors;
    }
    
    public void setCategoryName(final Integer categoryName) {
        this.categoryName = categoryName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataStallMenu)) {
            return false;
        }
        final SysDataStallMenu other = (SysDataStallMenu)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$parentSerial = this.getParentSerial();
        final Object other$parentSerial = other.getParentSerial();
        Label_0102: {
            if (this$parentSerial == null) {
                if (other$parentSerial == null) {
                    break Label_0102;
                }
            }
            else if (this$parentSerial.equals(other$parentSerial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$ancestors = this.getAncestors();
        final Object other$ancestors = other.getAncestors();
        Label_0139: {
            if (this$ancestors == null) {
                if (other$ancestors == null) {
                    break Label_0139;
                }
            }
            else if (this$ancestors.equals(other$ancestors)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$categoryName = this.getCategoryName();
        final Object other$categoryName = other.getCategoryName();
        if (this$categoryName == null) {
            if (other$categoryName == null) {
                return true;
            }
        }
        else if (this$categoryName.equals(other$categoryName)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataStallMenu;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $parentSerial = this.getParentSerial();
        result = result * 59 + (($parentSerial == null) ? 43 : $parentSerial.hashCode());
        final Object $ancestors = this.getAncestors();
        result = result * 59 + (($ancestors == null) ? 43 : $ancestors.hashCode());
        final Object $categoryName = this.getCategoryName();
        result = result * 59 + (($categoryName == null) ? 43 : $categoryName.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataStallMenu(serial=" + this.getSerial() + ", parentSerial=" + this.getParentSerial() + ", ancestors=" + this.getAncestors() + ", categoryName=" + this.getCategoryName() + ")";
    }
}
