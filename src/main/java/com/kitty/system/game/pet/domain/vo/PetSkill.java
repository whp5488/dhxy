package com.kitty.system.game.pet.domain.vo;

import java.io.Serializable;

public class PetSkill implements Serializable
{
    private int serial;
    private int type;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetSkill)) {
            return false;
        }
        PetSkill other = (PetSkill)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetSkill;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "PetSkill(serial=" + this.getSerial() + ", type=" + this.getType() + ")";
    }
    
    public PetSkill(final int serial, final int type) {
        this.serial = -1;
        this.type = 0;
        this.serial = serial;
        this.type = type;
    }
    
    public PetSkill() {
        this.serial = -1;
        this.type = 0;
    }
}
