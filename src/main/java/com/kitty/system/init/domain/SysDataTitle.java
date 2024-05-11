
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_title")
public class SysDataTitle implements Serializable
{
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("名称")
    @Column("name")
    private String name;
    @Comment("过期（秒）-1.自定义 0.为永久")
    @Column("expire")
    private int expire;
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getExpire() {
        return this.expire;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setExpire(final int expire) {
        this.expire = expire;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTitle)) {
            return false;
        }
        final SysDataTitle other = (SysDataTitle)o;
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
                return this.getExpire() == other.getExpire();
            }
        }
        else if (this$name.equals(other$name)) {
            return this.getExpire() == other.getExpire();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTitle;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getExpire();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTitle(serial=" + this.getSerial() + ", name=" + this.getName() + ", expire=" + this.getExpire() + ")";
    }
}
