package com.kitty.system.game.market.message.vo;

import java.io.Serializable;

public class PetEquipCard implements Serializable
{
    private String id;
    private int serial;
    private byte pj;
    
    public PetEquipCard() {
    }
    
    public PetEquipCard(long id, int serial, byte pj) {
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
        PetEquipCard other = (PetEquipCard)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getSerial() == other.getSerial() && this.getPj() == other.getPj();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PetEquipCard;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getPj();
        return result;
    }
    
    @Override
    public String toString() {
        return "PetEquipCard(id=" + this.getId() + ", serial=" + this.getSerial() + ", pj=" + (int)this.getPj() + ")";
    }
}
