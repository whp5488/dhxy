package com.kitty.system.game.fightnew.bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BeforeActionSkill
{
    private boolean isASide;
    private boolean passiveAction;
    private int round;
    private Map<Integer, List<Integer>> roleSkillMap;
    private List<Integer> skills;
    
    public BeforeActionSkill() {
        this.roleSkillMap = new LinkedHashMap();
        this.skills = new ArrayList<>();
    }
    
    public boolean isASide() {
        return this.isASide;
    }
    
    public boolean isPassiveAction() {
        return this.passiveAction;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public Map<Integer, List<Integer>> getRoleSkillMap() {
        return this.roleSkillMap;
    }
    
    public List<Integer> getSkills() {
        return this.skills;
    }
    
    public void setASide(final boolean isASide) {
        this.isASide = isASide;
    }
    
    public void setPassiveAction(final boolean passiveAction) {
        this.passiveAction = passiveAction;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setRoleSkillMap(final Map<Integer, List<Integer>> roleSkillMap) {
        this.roleSkillMap = roleSkillMap;
    }
    
    public void setSkills(final List<Integer> skills) {
        this.skills = skills;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof BeforeActionSkill)) {
            return false;
        }
        BeforeActionSkill other = (BeforeActionSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isASide() != other.isASide()) {
            return false;
        }
        if (this.isPassiveAction() != other.isPassiveAction()) {
            return false;
        }
        if (this.getRound() != other.getRound()) {
            return false;
        }
        Object this$roleSkillMap = this.getRoleSkillMap();
        Object other$roleSkillMap = other.getRoleSkillMap();
        if (!((this$roleSkillMap != null) ? this$roleSkillMap.equals(other$roleSkillMap) : (other$roleSkillMap == null))) {
            return false;
        }
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        return (this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof BeforeActionSkill;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isASide() ? 79 : 97);
        result = result * 59 + (this.isPassiveAction() ? 79 : 97);
        result = result * 59 + this.getRound();
        Object $roleSkillMap = this.getRoleSkillMap();
        result = result * 59 + (($roleSkillMap == null) ? 43 : $roleSkillMap.hashCode());
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "BeforeActionSkill(isASide=" + this.isASide() + ", passiveAction=" + this.isPassiveAction() + ", round=" + this.getRound() + ", roleSkillMap=" + this.getRoleSkillMap() + ", skills=" + this.getSkills() + ")";
    }
}
