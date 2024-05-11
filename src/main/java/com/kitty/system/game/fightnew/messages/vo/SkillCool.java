
package com.kitty.system.game.fightnew.messages.vo;

import java.io.Serializable;

public class SkillCool implements Serializable
{
    private int objId;
    private int skillId;
    private int round;
    
    public int getObjId() {
        return this.objId;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SkillCool)) {
            return false;
        }
        final SkillCool other = (SkillCool)o;
        return other.canEqual(this) && this.getObjId() == other.getObjId() && this.getSkillId() == other.getSkillId() && this.getRound() == other.getRound();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SkillCool;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getObjId();
        result = result * 59 + this.getSkillId();
        result = result * 59 + this.getRound();
        return result;
    }
    
    @Override
    public String toString() {
        return "SkillCool(objId=" + this.getObjId() + ", skillId=" + this.getSkillId() + ", round=" + this.getRound() + ")";
    }
}
