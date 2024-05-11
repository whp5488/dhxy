
package com.kitty.system.game.skill.message.vo;

import java.beans.ConstructorProperties;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.io.Serializable;

public class Skill implements Serializable
{
    private Integer skillId;
    private Integer type;
    private Integer proficient;
    private Integer maxProficient;
    
    public Skill(final RoleSkill roleSkill) {
        this.type = -1;
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
        final Skill other = (Skill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$skillId = this.getSkillId();
        final Object other$skillId = other.getSkillId();
        Label_0065: {
            if (this$skillId == null) {
                if (other$skillId == null) {
                    break Label_0065;
                }
            }
            else if (this$skillId.equals(other$skillId)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0102: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0102;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$proficient = this.getProficient();
        final Object other$proficient = other.getProficient();
        Label_0139: {
            if (this$proficient == null) {
                if (other$proficient == null) {
                    break Label_0139;
                }
            }
            else if (this$proficient.equals(other$proficient)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$maxProficient = this.getMaxProficient();
        final Object other$maxProficient = other.getMaxProficient();
        if (this$maxProficient == null) {
            if (other$maxProficient == null) {
                return true;
            }
        }
        else if (this$maxProficient.equals(other$maxProficient)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Skill;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $skillId = this.getSkillId();
        result = result * 59 + (($skillId == null) ? 43 : $skillId.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $proficient = this.getProficient();
        result = result * 59 + (($proficient == null) ? 43 : $proficient.hashCode());
        final Object $maxProficient = this.getMaxProficient();
        result = result * 59 + (($maxProficient == null) ? 43 : $maxProficient.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Skill(skillId=" + this.getSkillId() + ", type=" + this.getType() + ", proficient=" + this.getProficient() + ", maxProficient=" + this.getMaxProficient() + ")";
    }
    
    public Skill() {
        this.type = -1;
    }
    
    @ConstructorProperties({ "skillId", "type", "proficient", "maxProficient" })
    public Skill(final Integer skillId, final Integer type, final Integer proficient, final Integer maxProficient) {
        this.type = -1;
        this.skillId = skillId;
        this.type = type;
        this.proficient = proficient;
        this.maxProficient = maxProficient;
    }
}
