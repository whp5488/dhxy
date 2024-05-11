
package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class SkillParam implements Serializable
{
    private int skillId;
    private int proficient;
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setProficient(final int proficient) {
        this.proficient = proficient;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getProficient() {
        return this.proficient;
    }
    
    @ConstructorProperties({ "skillId", "proficient" })
    public SkillParam(final int skillId, final int proficient) {
        this.skillId = skillId;
        this.proficient = proficient;
    }
    
    public SkillParam() {
    }
}
