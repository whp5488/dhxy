
package com.kitty.system.game.skill.domain;

import java.beans.ConstructorProperties;
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
    
    public RoleSkill(final Integer skillId, final Integer type, final Integer proficient, final Integer maxProficient) {
        this.type = -1;
        this.effects = new ArrayList<FieldByte>();
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
        final RoleSkill other = (RoleSkill)o;
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
        Label_0176: {
            if (this$maxProficient == null) {
                if (other$maxProficient == null) {
                    break Label_0176;
                }
            }
            else if (this$maxProficient.equals(other$maxProficient)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$fightType = this.getFightType();
        final Object other$fightType = other.getFightType();
        Label_0213: {
            if (this$fightType == null) {
                if (other$fightType == null) {
                    break Label_0213;
                }
            }
            else if (this$fightType.equals(other$fightType)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$effects = this.getEffects();
        final Object other$effects = other.getEffects();
        if (this$effects == null) {
            if (other$effects == null) {
                return true;
            }
        }
        else if (this$effects.equals(other$effects)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RoleSkill;
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
        final Object $fightType = this.getFightType();
        result = result * 59 + (($fightType == null) ? 43 : $fightType.hashCode());
        final Object $effects = this.getEffects();
        result = result * 59 + (($effects == null) ? 43 : $effects.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RoleSkill(skillId=" + this.getSkillId() + ", type=" + this.getType() + ", proficient=" + this.getProficient() + ", maxProficient=" + this.getMaxProficient() + ", fightType=" + this.getFightType() + ", effects=" + this.getEffects() + ")";
    }
    
    public RoleSkill() {
        this.type = -1;
        this.effects = new ArrayList<FieldByte>();
    }
    
    @ConstructorProperties({ "skillId", "type", "proficient", "maxProficient", "fightType", "effects" })
    public RoleSkill(final Integer skillId, final Integer type, final Integer proficient, final Integer maxProficient, final Byte fightType, final List<FieldByte> effects) {
        this.type = -1;
        this.effects = new ArrayList<FieldByte>();
        this.skillId = skillId;
        this.type = type;
        this.proficient = proficient;
        this.maxProficient = maxProficient;
        this.fightType = fightType;
        this.effects = effects;
    }
}
