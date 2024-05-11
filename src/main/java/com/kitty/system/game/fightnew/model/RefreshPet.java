
package com.kitty.system.game.fightnew.model;

import java.io.Serializable;

public class RefreshPet implements Serializable
{
    private int petId;
    private int type;
    
    public RefreshPet() {
        this.type = 0;
    }
    
    public int getPetId() {
        return this.petId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RefreshPet)) {
            return false;
        }
        final RefreshPet other = (RefreshPet)o;
        return other.canEqual(this) && this.getPetId() == other.getPetId() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RefreshPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "RefreshPet(petId=" + this.getPetId() + ", type=" + this.getType() + ")";
    }
}
