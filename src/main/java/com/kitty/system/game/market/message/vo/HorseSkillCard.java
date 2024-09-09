package com.kitty.system.game.market.message.vo;

import java.io.Serializable;

public class HorseSkillCard implements Serializable
{
    private int growUp;
    private short level;
    private String skill;
    
    public HorseSkillCard() {
    }
    
    public HorseSkillCard(int growUp, short level, String skill) {
        this.growUp = growUp;
        this.level = level;
        this.skill = skill;
    }
    
    public int getGrowUp() {
        return this.growUp;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public String getSkill() {
        return this.skill;
    }
    
    public void setGrowUp(final int growUp) {
        this.growUp = growUp;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setSkill(final String skill) {
        this.skill = skill;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof HorseSkillCard)) {
            return false;
        }
        HorseSkillCard other = (HorseSkillCard)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getGrowUp() != other.getGrowUp()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        Object this$skill = this.getSkill();
        Object other$skill = other.getSkill();
        return (this$skill != null) ? this$skill.equals(other$skill) : (other$skill == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof HorseSkillCard;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGrowUp();
        result = result * 59 + this.getLevel();
        Object $skill = this.getSkill();
        result = result * 59 + (($skill == null) ? 43 : $skill.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "HorseSkillCard(growUp=" + this.getGrowUp() + ", level=" + (int)this.getLevel() + ", skill=" + this.getSkill() + ")";
    }
}
