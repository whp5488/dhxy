package com.kitty.system.game.skill.domain;

import java.util.ArrayList;
import com.kitty.system.game.vo.FieldByte;
import java.util.List;
import java.io.Serializable;

public class RoleSkill implements Serializable
{
    private Integer skillId;
    private Integer type;
    private Integer proficient;
    private Integer maxProficient;
    private Byte fightType;
    private List<FieldByte> effects;
    
    public RoleSkill(Integer skillId, Integer type, Integer proficient, Integer maxProficient) {
        this.type = Integer.valueOf(-1);
        this.effects = new ArrayList<>();
        this.skillId = skillId;
        this.type = type;
        this.proficient = proficient;
        this.maxProficient = maxProficient;
    }
    
    public Integer getSkillId() {
        return this.skillId;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getProficient() {
        return this.proficient;
    }
    
    public Integer getMaxProficient() {
        return this.maxProficient;
    }
    
    public Byte getFightType() {
        return this.fightType;
    }
    
    public List<FieldByte> getEffects() {
        return this.effects;
    }
    
    public void setSkillId(final Integer skillId) {
        this.skillId = skillId;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setProficient(final Integer proficient) {
        this.proficient = proficient;
    }
    
    public void setMaxProficient(final Integer maxProficient) {
        this.maxProficient = maxProficient;
    }
    
    public void setFightType(final Byte fightType) {
        this.fightType = fightType;
    }
    
    public void setEffects(final List<FieldByte> effects) {
        this.effects = effects;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RoleSkill)) {
            return false;
        }
        RoleSkill other = (RoleSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$skillId = this.getSkillId();
        Object other$skillId = other.getSkillId();
        if (!((this$skillId != null) ? this$skillId.equals(other$skillId) : (other$skillId == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$proficient = this.getProficient();
        Object other$proficient = other.getProficient();
        if (!((this$proficient != null) ? this$proficient.equals(other$proficient) : (other$proficient == null))) {
            return false;
        }
        Object this$maxProficient = this.getMaxProficient();
        Object other$maxProficient = other.getMaxProficient();
        if (!((this$maxProficient != null) ? this$maxProficient.equals(other$maxProficient) : (other$maxProficient == null))) {
            return false;
        }
        Object this$fightType = this.getFightType();
        Object other$fightType = other.getFightType();
        if (!((this$fightType != null) ? this$fightType.equals(other$fightType) : (other$fightType == null))) {
            return false;
        }
        Object this$effects = this.getEffects();
        Object other$effects = other.getEffects();
        return (this$effects != null) ? this$effects.equals(other$effects) : (other$effects == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RoleSkill;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $skillId = this.getSkillId();
        result = result * 59 + (($skillId == null) ? 43 : $skillId.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $proficient = this.getProficient();
        result = result * 59 + (($proficient == null) ? 43 : $proficient.hashCode());
        Object $maxProficient = this.getMaxProficient();
        result = result * 59 + (($maxProficient == null) ? 43 : $maxProficient.hashCode());
        Object $fightType = this.getFightType();
        result = result * 59 + (($fightType == null) ? 43 : $fightType.hashCode());
        Object $effects = this.getEffects();
        result = result * 59 + (($effects == null) ? 43 : $effects.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RoleSkill(skillId=" + this.getSkillId() + ", type=" + this.getType() + ", proficient=" + this.getProficient() + ", maxProficient=" + this.getMaxProficient() + ", fightType=" + this.getFightType() + ", effects=" + this.getEffects() + ")";
    }
    
    public RoleSkill() {
        this.type = Integer.valueOf(-1);
        this.effects = new ArrayList<>();
    }
    
    public RoleSkill(final Integer skillId, final Integer type, final Integer proficient, final Integer maxProficient, final Byte fightType, final List<FieldByte> effects) {
        this.type = Integer.valueOf(-1);
        this.effects = new ArrayList<>();
        this.skillId = skillId;
        this.type = type;
        this.proficient = proficient;
        this.maxProficient = maxProficient;
        this.fightType = fightType;
        this.effects = effects;
    }
}
