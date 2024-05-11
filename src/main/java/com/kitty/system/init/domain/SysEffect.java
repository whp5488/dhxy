
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_effect")
public class SysEffect
{
    @Column("serial")
    private int serial;
    @Column("name")
    private String name;
    @Column("part")
    private byte part;
    @Column("icon")
    private int icon;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getPart() {
        return this.part;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPart(final byte part) {
        this.part = part;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysEffect)) {
            return false;
        }
        final SysEffect other = (SysEffect)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return this.getPart() == other.getPart() && this.getIcon() == other.getIcon();
            }
        }
        else if (this$name.equals(other$name)) {
            return this.getPart() == other.getPart() && this.getIcon() == other.getIcon();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysEffect;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getPart();
        result = result * 59 + this.getIcon();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysEffect(serial=" + this.getSerial() + ", name=" + this.getName() + ", part=" + this.getPart() + ", icon=" + this.getIcon() + ")";
    }
}
