
package com.kitty.system.game.market.message.vo;

import java.io.Serializable;

public class PetEquipCard implements Serializable
{
    private String id;
    private int serial;
    private byte pj;
    
    public PetEquipCard() {
    }
    
    public PetEquipCard(final long id, final int serial, final byte pj) {
        this.id = String.valueOf(id);
        this.serial = serial;
        this.pj = pj;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getPj() {
        return this.pj;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPj(final byte pj) {
        this.pj = pj;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetEquipCard)) {
            return false;
        }
        final PetEquipCard other = (PetEquipCard)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getSerial() == other.getSerial() && this.getPj() == other.getPj();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getSerial() == other.getSerial() && this.getPj() == other.getPj();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetEquipCard;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getPj();
        return result;
    }
    
    @Override
    public String toString() {
        return "PetEquipCard(id=" + this.getId() + ", serial=" + this.getSerial() + ", pj=" + this.getPj() + ")";
    }
}
