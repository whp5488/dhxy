package com.kitty.system.game.skill.domain;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;

public class SkillBox implements Serializable
{
    private Map<Integer, RoleSkill> skills;
    
    public SkillBox() {
        this.skills = new HashMap();
    }
    
    public Map<Integer, RoleSkill> getSkills() {
        return this.skills;
    }
    
    public void setSkills(final Map<Integer, RoleSkill> skills) {
        this.skills = skills;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SkillBox)) {
            return false;
        }
        SkillBox other = (SkillBox)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        return (this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SkillBox;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SkillBox(skills=" + this.getSkills() + ")";
    }
}
