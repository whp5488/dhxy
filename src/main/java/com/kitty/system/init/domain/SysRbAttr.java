
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
        final SysRbAttr other = (SysRbAttr)o;
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
        final Object this$lvLimit = this.getLvLimit();
        final Object other$lvLimit = other.getLvLimit();
        Label_0102: {
            if (this$lvLimit == null) {
                if (other$lvLimit == null) {
                    break Label_0102;
                }
            }
            else if (this$lvLimit.equals(other$lvLimit)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$initSkillExp = this.getInitSkillExp();
        final Object other$initSkillExp = other.getInitSkillExp();
        Label_0139: {
            if (this$initSkillExp == null) {
                if (other$initSkillExp == null) {
                    break Label_0139;
                }
            }
            else if (this$initSkillExp.equals(other$initSkillExp)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$maxSkillExp = this.getMaxSkillExp();
        final Object other$maxSkillExp = other.getMaxSkillExp();
        Label_0176: {
            if (this$maxSkillExp == null) {
                if (other$maxSkillExp == null) {
                    break Label_0176;
                }
            }
            else if (this$maxSkillExp.equals(other$maxSkillExp)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$initLv = this.getInitLv();
        final Object other$initLv = other.getInitLv();
        Label_0213: {
            if (this$initLv == null) {
                if (other$initLv == null) {
                    break Label_0213;
                }
            }
            else if (this$initLv.equals(other$initLv)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$petInitLv = this.getPetInitLv();
        final Object other$petInitLv = other.getPetInitLv();
        Label_0250: {
            if (this$petInitLv == null) {
                if (other$petInitLv == null) {
                    break Label_0250;
                }
            }
            else if (this$petInitLv.equals(other$petInitLv)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$petNum = this.getPetNum();
        final Object other$petNum = other.getPetNum();
        if (this$petNum == null) {
            if (other$petNum == null) {
                return true;
            }
        }
        else if (this$petNum.equals(other$petNum)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysRbAttr;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        final Object $initSkillExp = this.getInitSkillExp();
        result = result * 59 + (($initSkillExp == null) ? 43 : $initSkillExp.hashCode());
        final Object $maxSkillExp = this.getMaxSkillExp();
        result = result * 59 + (($maxSkillExp == null) ? 43 : $maxSkillExp.hashCode());
        final Object $initLv = this.getInitLv();
        result = result * 59 + (($initLv == null) ? 43 : $initLv.hashCode());
        final Object $petInitLv = this.getPetInitLv();
        result = result * 59 + (($petInitLv == null) ? 43 : $petInitLv.hashCode());
        final Object $petNum = this.getPetNum();
        result = result * 59 + (($petNum == null) ? 43 : $petNum.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysRbAttr(id=" + this.getId() + ", lvLimit=" + this.getLvLimit() + ", initSkillExp=" + this.getInitSkillExp() + ", maxSkillExp=" + this.getMaxSkillExp() + ", initLv=" + this.getInitLv() + ", petInitLv=" + this.getPetInitLv() + ", petNum=" + this.getPetNum() + ")";
    }
}
