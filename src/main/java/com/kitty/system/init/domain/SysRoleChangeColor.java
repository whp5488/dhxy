
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
        final SysRoleChangeColor other = (SysRoleChangeColor)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0102: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0102;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$colorOne = this.getColorOne();
        final Object other$colorOne = other.getColorOne();
        Label_0139: {
            if (this$colorOne == null) {
                if (other$colorOne == null) {
                    break Label_0139;
                }
            }
            else if (this$colorOne.equals(other$colorOne)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$colorTwo = this.getColorTwo();
        final Object other$colorTwo = other.getColorTwo();
        if (this$colorTwo == null) {
            if (other$colorTwo == null) {
                return true;
            }
        }
        else if (this$colorTwo.equals(other$colorTwo)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysRoleChangeColor;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $colorOne = this.getColorOne();
        result = result * 59 + (($colorOne == null) ? 43 : $colorOne.hashCode());
        final Object $colorTwo = this.getColorTwo();
        result = result * 59 + (($colorTwo == null) ? 43 : $colorTwo.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysRoleChangeColor(id=" + this.getId() + ", serial=" + this.getSerial() + ", colorOne=" + this.getColorOne() + ", colorTwo=" + this.getColorTwo() + ")";
    }
}
