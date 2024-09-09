package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("h_horse")
public class SysHorse implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("坐骑名称")
    @Column("name")
    private String name;
    @Comment("骑乘等级")
    @Column("need_grade")
    private int needGrade;
    @Comment("骑乘角色")
    @Column("need_serial")
    @Default("[]")
    private List<Integer> needSerial;
    @Comment("坐骑位置")
    @Column("seat")
    private byte seat;
    
    public SysHorse() {
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getNeedGrade() {
        return this.needGrade;
    }
    
    public List<Integer> getNeedSerial() {
        return this.needSerial;
    }
    
    public byte getSeat() {
        return this.seat;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setNeedGrade(final int needGrade) {
        this.needGrade = needGrade;
    }
    
    public void setNeedSerial(final List<Integer> needSerial) {
        this.needSerial = needSerial;
    }
    
    public void setSeat(final byte seat) {
        this.seat = seat;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysHorse)) {
            return false;
        }
        SysHorse other = (SysHorse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        if (this.getNeedGrade() != other.getNeedGrade()) {
            return false;
        }
        Object this$needSerial = this.getNeedSerial();
        Object other$needSerial = other.getNeedSerial();
        return ((this$needSerial != null) ? this$needSerial.equals(other$needSerial) : (other$needSerial == null)) && this.getSeat() == other.getSeat();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysHorse;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getNeedGrade();
        Object $needSerial = this.getNeedSerial();
        result = result * 59 + (($needSerial == null) ? 43 : $needSerial.hashCode());
        result = result * 59 + this.getSeat();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysHorse(serial=" + this.getSerial() + ", name=" + this.getName() + ", needGrade=" + this.getNeedGrade() + ", needSerial=" + this.getNeedSerial() + ", seat=" + (int)this.getSeat() + ")";
    }
}
