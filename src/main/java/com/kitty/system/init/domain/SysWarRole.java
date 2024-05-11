
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
        final SysWarRole other = (SysWarRole)o;
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
        final Object this$des = this.getDes();
        final Object other$des = other.getDes();
        Label_0139: {
            if (this$des == null) {
                if (other$des == null) {
                    break Label_0139;
                }
            }
            else if (this$des.equals(other$des)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$monster = this.getMonster();
        final Object other$monster = other.getMonster();
        Label_0176: {
            if (this$monster == null) {
                if (other$monster == null) {
                    break Label_0176;
                }
            }
            else if (this$monster.equals(other$monster)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$summon = this.getSummon();
        final Object other$summon = other.getSummon();
        if (this$summon == null) {
            if (other$summon == null) {
                return this.getSpecialType() == other.getSpecialType();
            }
        }
        else if (this$summon.equals(other$summon)) {
            return this.getSpecialType() == other.getSpecialType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysWarRole;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $des = this.getDes();
        result = result * 59 + (($des == null) ? 43 : $des.hashCode());
        final Object $monster = this.getMonster();
        result = result * 59 + (($monster == null) ? 43 : $monster.hashCode());
        final Object $summon = this.getSummon();
        result = result * 59 + (($summon == null) ? 43 : $summon.hashCode());
        result = result * 59 + this.getSpecialType();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysWarRole(id=" + this.getId() + ", serial=" + this.getSerial() + ", des=" + this.getDes() + ", monster=" + this.getMonster() + ", summon=" + this.getSummon() + ", specialType=" + this.getSpecialType() + ")";
    }
}
