
package com.kitty.system.game.pet.domain.vo;

import java.io.Serializable;

public class PetCorrection implements Serializable
{
    private int level;
    private long exp;
    private int points;
    
    public PetCorrection() {
        this.level = 0;
        this.exp = 0L;
        this.points = 0;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public long getExp() {
        return this.exp;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setExp(final long exp) {
        this.exp = exp;
    }
    
    public void setPoints(final int points) {
        this.points = points;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetCorrection)) {
            return false;
        }
        final PetCorrection other = (PetCorrection)o;
        return other.canEqual(this) && this.getLevel() == other.getLevel() && this.getExp() == other.getExp() && this.getPoints() == other.getPoints();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetCorrection;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLevel();
        final long $exp = this.getExp();
        result = result * 59 + (int)($exp >>> 32 ^ $exp);
        result = result * 59 + this.getPoints();
        return result;
    }
    
    @Override
    public String toString() {
        return "PetCorrection(level=" + this.getLevel() + ", exp=" + this.getExp() + ", points=" + this.getPoints() + ")";
    }
}
