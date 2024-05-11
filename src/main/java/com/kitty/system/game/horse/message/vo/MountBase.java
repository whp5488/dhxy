
package com.kitty.system.game.horse.message.vo;

import java.beans.ConstructorProperties;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class MountBase implements Serializable
{
    private int grow_up;
    private int move_sp;
    private int score;
    private List<FiedValue> skill;
    
    public int getGrow_up() {
        return this.grow_up;
    }
    
    public int getMove_sp() {
        return this.move_sp;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public List<FiedValue> getSkill() {
        return this.skill;
    }
    
    public void setGrow_up(final int grow_up) {
        this.grow_up = grow_up;
    }
    
    public void setMove_sp(final int move_sp) {
        this.move_sp = move_sp;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setSkill(final List<FiedValue> skill) {
        this.skill = skill;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MountBase)) {
            return false;
        }
        final MountBase other = (MountBase)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getGrow_up() != other.getGrow_up()) {
            return false;
        }
        if (this.getMove_sp() != other.getMove_sp()) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        final Object this$skill = this.getSkill();
        final Object other$skill = other.getSkill();
        if (this$skill == null) {
            if (other$skill == null) {
                return true;
            }
        }
        else if (this$skill.equals(other$skill)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MountBase;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getGrow_up();
        result = result * 59 + this.getMove_sp();
        result = result * 59 + this.getScore();
        final Object $skill = this.getSkill();
        result = result * 59 + (($skill == null) ? 43 : $skill.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MountBase(grow_up=" + this.getGrow_up() + ", move_sp=" + this.getMove_sp() + ", score=" + this.getScore() + ", skill=" + this.getSkill() + ")";
    }
    
    @ConstructorProperties({ "grow_up", "move_sp", "score", "skill" })
    public MountBase(final int grow_up, final int move_sp, final int score, final List<FiedValue> skill) {
        this.score = 0;
        this.grow_up = grow_up;
        this.move_sp = move_sp;
        this.score = score;
        this.skill = skill;
    }
    
    public MountBase() {
        this.score = 0;
    }
}
