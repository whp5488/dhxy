
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4230)
public class ReqDropPet extends Message
{
    private int petId;
    
    public int getPetId() {
        return this.petId;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqDropPet)) {
            return false;
        }
        final ReqDropPet other = (ReqDropPet)o;
        return other.canEqual(this) && this.getPetId() == other.getPetId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqDropPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqDropPet(petId=" + this.getPetId() + ")";
    }
}
