package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("role_change_color")
public class SysRoleChangeColor implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("角色外观编号")
    @Column("serial")
    private Integer serial;
    @Comment("部位-1")
    @Column("color_one")
    private String colorOne;
    @Comment("部位-2")
    @Column("color_two")
    private String colorTwo;
    
    public SysRoleChangeColor() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getColorOne() {
        return this.colorOne;
    }
    
    public String getColorTwo() {
        return this.colorTwo;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setColorOne(final String colorOne) {
        this.colorOne = colorOne;
    }
    
    public void setColorTwo(final String colorTwo) {
        this.colorTwo = colorTwo;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysRoleChangeColor)) {
            return false;
        }
        SysRoleChangeColor other = (SysRoleChangeColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$colorOne = this.getColorOne();
        Object other$colorOne = other.getColorOne();
        if (!((this$colorOne != null) ? this$colorOne.equals(other$colorOne) : (other$colorOne == null))) {
            return false;
        }
        Object this$colorTwo = this.getColorTwo();
        Object other$colorTwo = other.getColorTwo();
        return (this$colorTwo != null) ? this$colorTwo.equals(other$colorTwo) : (other$colorTwo == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysRoleChangeColor;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $colorOne = this.getColorOne();
        result = result * 59 + (($colorOne == null) ? 43 : $colorOne.hashCode());
        Object $colorTwo = this.getColorTwo();
        result = result * 59 + (($colorTwo == null) ? 43 : $colorTwo.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysRoleChangeColor(id=" + this.getId() + ", serial=" + this.getSerial() + ", colorOne=" + this.getColorOne() + ", colorTwo=" + this.getColorTwo() + ")";
    }
}
