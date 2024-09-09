package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45338)
public class RespPetCorrection extends Message
{
    private Integer petId;
    private int level;
    private String exp;
    private int points;
    private String nutMap;
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public String getExp() {
        return this.exp;
    }
    
    public int getPoints() {
        return this.points;
    }
    
    public String getNutMap() {
        return this.nutMap;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setExp(final String exp) {
        this.exp = exp;
    }
    
    public void setPoints(final int points) {
        this.points = points;
    }
    
    public void setNutMap(final String nutMap) {
        this.nutMap = nutMap;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPetCorrection)) {
            return false;
        }
        RespPetCorrection other = (RespPetCorrection)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$petId = this.getPetId();
        Object other$petId = other.getPetId();
        if (!((this$petId != null) ? this$petId.equals(other$petId) : (other$petId == null))) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        Object this$exp = this.getExp();
        Object other$exp = other.getExp();
        if (!((this$exp != null) ? this$exp.equals(other$exp) : (other$exp == null))) {
            return false;
        }
        if (this.getPoints() != other.getPoints()) {
            return false;
        }
        Object this$nutMap = this.getNutMap();
        Object other$nutMap = other.getNutMap();
        return (this$nutMap != null) ? this$nutMap.equals(other$nutMap) : (other$nutMap == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetCorrection;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        result = result * 59 + this.getLevel();
        Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        result = result * 59 + this.getPoints();
        Object $nutMap = this.getNutMap();
        result = result * 59 + (($nutMap == null) ? 43 : $nutMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetCorrection(petId=" + this.getPetId() + ", level=" + this.getLevel() + ", exp=" + this.getExp() + ", points=" + this.getPoints() + ", nutMap=" + this.getNutMap() + ")";
    }
    
    public RespPetCorrection(final Integer petId, final int level, final String exp, final int points, final String nutMap) {
        this.level = 0;
        this.exp = "0";
        this.points = 0;
        this.petId = petId;
        this.level = level;
        this.exp = exp;
        this.points = points;
        this.nutMap = nutMap;
    }
    
    public RespPetCorrection() {
        this.level = 0;
        this.exp = "0";
        this.points = 0;
    }
}
