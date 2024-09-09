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
    
    public SysDataStallMenu() {
    }
    
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
        SysDataStallMenu other = (SysDataStallMenu)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$parentSerial = this.getParentSerial();
        Object other$parentSerial = other.getParentSerial();
        if (!((this$parentSerial != null) ? this$parentSerial.equals(other$parentSerial) : (other$parentSerial == null))) {
            return false;
        }
        Object this$ancestors = this.getAncestors();
        Object other$ancestors = other.getAncestors();
        if (!((this$ancestors != null) ? this$ancestors.equals(other$ancestors) : (other$ancestors == null))) {
            return false;
        }
        Object this$categoryName = this.getCategoryName();
        Object other$categoryName = other.getCategoryName();
        return (this$categoryName != null) ? this$categoryName.equals(other$categoryName) : (other$categoryName == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataStallMenu;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $parentSerial = this.getParentSerial();
        result = result * 59 + (($parentSerial == null) ? 43 : $parentSerial.hashCode());
        Object $ancestors = this.getAncestors();
        result = result * 59 + (($ancestors == null) ? 43 : $ancestors.hashCode());
        Object $categoryName = this.getCategoryName();
        result = result * 59 + (($categoryName == null) ? 43 : $categoryName.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataStallMenu(serial=" + this.getSerial() + ", parentSerial=" + this.getParentSerial() + ", ancestors=" + this.getAncestors() + ", categoryName=" + this.getCategoryName() + ")";
    }
}
