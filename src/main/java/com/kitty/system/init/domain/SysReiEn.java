package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.Field;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_rei_en")
public class SysReiEn implements Serializable
{
    @Comment("key")
    @Column("key")
    private int key;
    @Comment("转生")
    @Column("rei")
    private int rei;
    @Comment("种族")
    @Column("race")
    private int race;
    @Comment("性别")
    @Column("sex")
    private int sex;
    @Comment("数据编号")
    @Column("serial")
    private int serial;
    @Comment("修正属性")
    @Column("rei_en")
    @Default("[]")
    private List<Field> reiEn;
    
    public SysReiEn() {
    }
    
    public int getKey() {
        return this.key;
    }
    
    public int getRei() {
        return this.rei;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getSex() {
        return this.sex;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<Field> getReiEn() {
        return this.reiEn;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    public void setRei(final int rei) {
        this.rei = rei;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setSex(final int sex) {
        this.sex = sex;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setReiEn(final List<Field> reiEn) {
        this.reiEn = reiEn;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysReiEn)) {
            return false;
        }
        SysReiEn other = (SysReiEn)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getKey() != other.getKey()) {
            return false;
        }
        if (this.getRei() != other.getRei()) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getSex() != other.getSex()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$reiEn = this.getReiEn();
        Object other$reiEn = other.getReiEn();
        return (this$reiEn != null) ? this$reiEn.equals(other$reiEn) : (other$reiEn == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysReiEn;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getKey();
        result = result * 59 + this.getRei();
        result = result * 59 + this.getRace();
        result = result * 59 + this.getSex();
        result = result * 59 + this.getSerial();
        Object $reiEn = this.getReiEn();
        result = result * 59 + (($reiEn == null) ? 43 : $reiEn.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysReiEn(key=" + this.getKey() + ", rei=" + this.getRei() + ", race=" + this.getRace() + ", sex=" + this.getSex() + ", serial=" + this.getSerial() + ", reiEn=" + this.getReiEn() + ")";
    }
}
