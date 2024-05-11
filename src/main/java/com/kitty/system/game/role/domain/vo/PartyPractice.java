
package com.kitty.system.game.role.domain.vo;

import java.io.Serializable;

public class PartyPractice implements Serializable
{
    private int level;
    private int points;
    
    public PartyPractice() {
        this.level = 0;
        this.points = 0;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setPoints(final int points) {
        this.points = points;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PartyPractice)) {
            return false;
        }
        final PartyPractice other = (PartyPractice)o;
        return other.canEqual(this) && this.getLevel() == other.getLevel() && this.getPoints() == other.getPoints();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PartyPractice;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getPoints();
        return result;
    }
    
    @Override
    public String toString() {
        return "PartyPractice(level=" + this.getLevel() + ", points=" + this.getPoints() + ")";
    }
}
