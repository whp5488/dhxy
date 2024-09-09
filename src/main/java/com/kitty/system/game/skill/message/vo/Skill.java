package com.kitty.system.game.skill.message.vo;

import com.kitty.system.game.skill.domain.RoleSkill;
import java.io.Serializable;

public class Skill implements Serializable
{
    private Integer skillId;
    private Integer type;
    private Integer proficient;
    private Integer maxProficient;
    
    public Skill(RoleSkill roleSkill) {
        this.type = Integer.valueOf(-1);
        this.skillId = roleSkill.getSkillId();
        this.type = roleSkill.getType();
        this.proficient = roleSkill.getProficient();
        this.maxProficient = roleSkill.getMaxProficient();
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Skill)) {
            return false;
        }
        Skill other = (Skill)o;
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
        return (this$maxProficient != null) ? this$maxProficient.equals(other$maxProficient) : (other$maxProficient == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Skill;
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
        return result;
    }
    
    @Override
    public String toString() {
        return "Skill(skillId=" + this.getSkillId() + ", type=" + this.getType() + ", proficient=" + this.getProficient() + ", maxProficient=" + this.getMaxProficient() + ")";
    }
    
    public Skill() {
        this.type = Integer.valueOf(-1);
    }
    
    public Skill(final Integer skillId, final Integer type, final Integer proficient, final Integer maxProficient) {
        this.type = Integer.valueOf(-1);
        this.skillId = skillId;
        this.type = type;
        this.proficient = proficient;
        this.maxProficient = maxProficient;
    }
}
