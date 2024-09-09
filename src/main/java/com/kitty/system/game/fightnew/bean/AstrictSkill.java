package com.kitty.system.game.fightnew.bean;

import java.util.HashMap;

public class AstrictSkill
{
    private boolean isASide;
    private int round;
    private int time;
    private HashMap<Integer, Integer> skillMap;
    
    public AstrictSkill() {
        this.time = 1;
        this.skillMap = new HashMap();
    }
    
    public boolean isASide() {
        return this.isASide;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public HashMap<Integer, Integer> getSkillMap() {
        return this.skillMap;
    }
    
    public void setASide(final boolean isASide) {
        this.isASide = isASide;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setSkillMap(final HashMap<Integer, Integer> skillMap) {
        this.skillMap = skillMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AstrictSkill)) {
            return false;
        }
        AstrictSkill other = (AstrictSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isASide() != other.isASide()) {
            return false;
        }
        if (this.getRound() != other.getRound()) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        Object this$skillMap = this.getSkillMap();
        Object other$skillMap = other.getSkillMap();
        return (this$skillMap != null) ? this$skillMap.equals(other$skillMap) : (other$skillMap == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AstrictSkill;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isASide() ? 79 : 97);
        result = result * 59 + this.getRound();
        result = result * 59 + this.getTime();
        Object $skillMap = this.getSkillMap();
        result = result * 59 + (($skillMap == null) ? 43 : $skillMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "AstrictSkill(isASide=" + this.isASide() + ", round=" + this.getRound() + ", time=" + this.getTime() + ", skillMap=" + this.getSkillMap() + ")";
    }
}
