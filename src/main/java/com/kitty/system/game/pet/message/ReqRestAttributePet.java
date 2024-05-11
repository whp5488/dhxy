
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14340)
public class ReqRestAttributePet extends Message
{
    private Integer petId;
    private byte type;
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqRestAttributePet)) {
            return false;
        }
        final ReqRestAttributePet other = (ReqRestAttributePet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$petId = this.getPetId();
        final Object other$petId = other.getPetId();
        if (this$petId == null) {
            if (other$petId == null) {
                return this.getType() == other.getType();
            }
        }
        else if (this$petId.equals(other$petId)) {
            return this.getType() == other.getType();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRestAttributePet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRestAttributePet(petId=" + this.getPetId() + ", type=" + this.getType() + ")";
    }
}
