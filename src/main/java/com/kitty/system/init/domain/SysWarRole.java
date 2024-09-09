package com.kitty.system.init.domain;

import com.kitty.system.game.vo.init.Summon;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.init.Monster;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_war_role")
public class SysWarRole implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("说明")
    @Column("des")
    private String des;
    @Comment("怪物数据")
    @Column("monster")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<Monster> monster;
    @Comment("召唤小怪")
    @Column("summon")
    @ColDefine(customType = "longtext")
    private Summon summon;
    @Comment("特殊类型")
    @Column("special_type")
    private byte specialType;
    
    public SysWarRole() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getDes() {
        return this.des;
    }
    
    public List<Monster> getMonster() {
        return this.monster;
    }
    
    public Summon getSummon() {
        return this.summon;
    }
    
    public byte getSpecialType() {
        return this.specialType;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setDes(final String des) {
        this.des = des;
    }
    
    public void setMonster(final List<Monster> monster) {
        this.monster = monster;
    }
    
    public void setSummon(final Summon summon) {
        this.summon = summon;
    }
    
    public void setSpecialType(final byte specialType) {
        this.specialType = specialType;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysWarRole)) {
            return false;
        }
        SysWarRole other = (SysWarRole)o;
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
        Object this$des = this.getDes();
        Object other$des = other.getDes();
        if (!((this$des != null) ? this$des.equals(other$des) : (other$des == null))) {
            return false;
        }
        Object this$monster = this.getMonster();
        Object other$monster = other.getMonster();
        if (!((this$monster != null) ? this$monster.equals(other$monster) : (other$monster == null))) {
            return false;
        }
        Object this$summon = this.getSummon();
        Object other$summon = other.getSummon();
        return ((this$summon != null) ? this$summon.equals(other$summon) : (other$summon == null)) && this.getSpecialType() == other.getSpecialType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysWarRole;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $des = this.getDes();
        result = result * 59 + (($des == null) ? 43 : $des.hashCode());
        Object $monster = this.getMonster();
        result = result * 59 + (($monster == null) ? 43 : $monster.hashCode());
        Object $summon = this.getSummon();
        result = result * 59 + (($summon == null) ? 43 : $summon.hashCode());
        result = result * 59 + this.getSpecialType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysWarRole(id=" + this.getId() + ", serial=" + this.getSerial() + ", des=" + this.getDes() + ", monster=" + this.getMonster() + ", summon=" + this.getSummon() + ", specialType=" + (int)this.getSpecialType() + ")";
    }
}
