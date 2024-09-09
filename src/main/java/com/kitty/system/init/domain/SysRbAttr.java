package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_rb_attr")
public class SysRbAttr implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("等级经验")
    @Column("lv_limit")
    private Integer lvLimit;
    @Comment("初始技能经验")
    @Column("init_skill_exp")
    private Integer initSkillExp;
    @Comment("技能最大技能经验")
    @Column("max_skill_exp")
    private Integer maxSkillExp;
    @Comment("转生后初始等级")
    @Column("init_lv")
    private Integer initLv;
    @Comment("宠物转生后初始等级")
    @Column("pet_init_lv")
    private Integer petInitLv;
    @Comment("携带宠物上限")
    @Column("pet_num")
    private Integer petNum;
    
    public SysRbAttr() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getLvLimit() {
        return this.lvLimit;
    }
    
    public Integer getInitSkillExp() {
        return this.initSkillExp;
    }
    
    public Integer getMaxSkillExp() {
        return this.maxSkillExp;
    }
    
    public Integer getInitLv() {
        return this.initLv;
    }
    
    public Integer getPetInitLv() {
        return this.petInitLv;
    }
    
    public Integer getPetNum() {
        return this.petNum;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setLvLimit(final Integer lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setInitSkillExp(final Integer initSkillExp) {
        this.initSkillExp = initSkillExp;
    }
    
    public void setMaxSkillExp(final Integer maxSkillExp) {
        this.maxSkillExp = maxSkillExp;
    }
    
    public void setInitLv(final Integer initLv) {
        this.initLv = initLv;
    }
    
    public void setPetInitLv(final Integer petInitLv) {
        this.petInitLv = petInitLv;
    }
    
    public void setPetNum(final Integer petNum) {
        this.petNum = petNum;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysRbAttr)) {
            return false;
        }
        SysRbAttr other = (SysRbAttr)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$lvLimit = this.getLvLimit();
        Object other$lvLimit = other.getLvLimit();
        if (!((this$lvLimit != null) ? this$lvLimit.equals(other$lvLimit) : (other$lvLimit == null))) {
            return false;
        }
        Object this$initSkillExp = this.getInitSkillExp();
        Object other$initSkillExp = other.getInitSkillExp();
        if (!((this$initSkillExp != null) ? this$initSkillExp.equals(other$initSkillExp) : (other$initSkillExp == null))) {
            return false;
        }
        Object this$maxSkillExp = this.getMaxSkillExp();
        Object other$maxSkillExp = other.getMaxSkillExp();
        if (!((this$maxSkillExp != null) ? this$maxSkillExp.equals(other$maxSkillExp) : (other$maxSkillExp == null))) {
            return false;
        }
        Object this$initLv = this.getInitLv();
        Object other$initLv = other.getInitLv();
        if (!((this$initLv != null) ? this$initLv.equals(other$initLv) : (other$initLv == null))) {
            return false;
        }
        Object this$petInitLv = this.getPetInitLv();
        Object other$petInitLv = other.getPetInitLv();
        if (!((this$petInitLv != null) ? this$petInitLv.equals(other$petInitLv) : (other$petInitLv == null))) {
            return false;
        }
        Object this$petNum = this.getPetNum();
        Object other$petNum = other.getPetNum();
        return (this$petNum != null) ? this$petNum.equals(other$petNum) : (other$petNum == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysRbAttr;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        Object $initSkillExp = this.getInitSkillExp();
        result = result * 59 + (($initSkillExp == null) ? 43 : $initSkillExp.hashCode());
        Object $maxSkillExp = this.getMaxSkillExp();
        result = result * 59 + (($maxSkillExp == null) ? 43 : $maxSkillExp.hashCode());
        Object $initLv = this.getInitLv();
        result = result * 59 + (($initLv == null) ? 43 : $initLv.hashCode());
        Object $petInitLv = this.getPetInitLv();
        result = result * 59 + (($petInitLv == null) ? 43 : $petInitLv.hashCode());
        Object $petNum = this.getPetNum();
        result = result * 59 + (($petNum == null) ? 43 : $petNum.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysRbAttr(id=" + this.getId() + ", lvLimit=" + this.getLvLimit() + ", initSkillExp=" + this.getInitSkillExp() + ", maxSkillExp=" + this.getMaxSkillExp() + ", initLv=" + this.getInitLv() + ", petInitLv=" + this.getPetInitLv() + ", petNum=" + this.getPetNum() + ")";
    }
}
