
package com.kitty.system.game.pet.message;

import java.beans.ConstructorProperties;
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
        final RespPetCorrection other = (RespPetCorrection)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$petId = this.getPetId();
        final Object other$petId = other.getPetId();
        Label_0065: {
            if (this$petId == null) {
                if (other$petId == null) {
                    break Label_0065;
                }
            }
            else if (this$petId.equals(other$petId)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        final Object this$exp = this.getExp();
        final Object other$exp = other.getExp();
        Label_0115: {
            if (this$exp == null) {
                if (other$exp == null) {
                    break Label_0115;
                }
            }
            else if (this$exp.equals(other$exp)) {
                break Label_0115;
            }
            return false;
        }
        if (this.getPoints() != other.getPoints()) {
            return false;
        }
        final Object this$nutMap = this.getNutMap();
        final Object other$nutMap = other.getNutMap();
        if (this$nutMap == null) {
            if (other$nutMap == null) {
                return true;
            }
        }
        else if (this$nutMap.equals(other$nutMap)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetCorrection;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        result = result * 59 + this.getLevel();
        final Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        result = result * 59 + this.getPoints();
        final Object $nutMap = this.getNutMap();
        result = result * 59 + (($nutMap == null) ? 43 : $nutMap.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetCorrection(petId=" + this.getPetId() + ", level=" + this.getLevel() + ", exp=" + this.getExp() + ", points=" + this.getPoints() + ", nutMap=" + this.getNutMap() + ")";
    }
    
    @ConstructorProperties({ "petId", "level", "exp", "points", "nutMap" })
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
