
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14342)
public class ReqChangeColorPet extends Message
{
    private int petId;
    private byte colorSerial;
    
    public int getPetId() {
        return this.petId;
    }
    
    public byte getColorSerial() {
        return this.colorSerial;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setColorSerial(final byte colorSerial) {
        this.colorSerial = colorSerial;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqChangeColorPet)) {
            return false;
        }
        final ReqChangeColorPet other = (ReqChangeColorPet)o;
        return other.canEqual(this) && this.getPetId() == other.getPetId() && this.getColorSerial() == other.getColorSerial();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqChangeColorPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        result = result * 59 + this.getColorSerial();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChangeColorPet(petId=" + this.getPetId() + ", colorSerial=" + this.getColorSerial() + ")";
    }
}
