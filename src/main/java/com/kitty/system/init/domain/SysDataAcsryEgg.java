
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry_egg")
public class SysDataAcsryEgg
{
    @Column("serial")
    private int serial;
    @Column("hatch_type")
    private int hatchType;
    @Column("name")
    private String name;
    @Column("can_merge")
    private int canMerge;
    @Column("rarity")
    private int rarity;
    @Column("shape")
    private int shape;
    
    public SysDataAcsryEgg() {
        this.canMerge = 1;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getHatchType() {
        return this.hatchType;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getCanMerge() {
        return this.canMerge;
    }
    
    public int getRarity() {
        return this.rarity;
    }
    
    public int getShape() {
        return this.shape;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setHatchType(final int hatchType) {
        this.hatchType = hatchType;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setCanMerge(final int canMerge) {
        this.canMerge = canMerge;
    }
    
    public void setRarity(final int rarity) {
        this.rarity = rarity;
    }
    
    public void setShape(final int shape) {
        this.shape = shape;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataAcsryEgg)) {
            return false;
        }
        final SysDataAcsryEgg other = (SysDataAcsryEgg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getHatchType() != other.getHatchType()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return this.getCanMerge() == other.getCanMerge() && this.getRarity() == other.getRarity() && this.getShape() == other.getShape();
            }
        }
        else if (this$name.equals(other$name)) {
            return this.getCanMerge() == other.getCanMerge() && this.getRarity() == other.getRarity() && this.getShape() == other.getShape();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsryEgg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getHatchType();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getCanMerge();
        result = result * 59 + this.getRarity();
        result = result * 59 + this.getShape();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsryEgg(serial=" + this.getSerial() + ", hatchType=" + this.getHatchType() + ", name=" + this.getName() + ", canMerge=" + this.getCanMerge() + ", rarity=" + this.getRarity() + ", shape=" + this.getShape() + ")";
    }
}
