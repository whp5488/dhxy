
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41153)
public class ReqRequestUpgradeTaskPet extends Message
{
    private int petId;
    private int param;
    
    public int getPetId() {
        return this.petId;
    }
    
    public int getParam() {
        return this.param;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setParam(final int param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqRequestUpgradeTaskPet)) {
            return false;
        }
        final ReqRequestUpgradeTaskPet other = (ReqRequestUpgradeTaskPet)o;
        return other.canEqual(this) && this.getPetId() == other.getPetId() && this.getParam() == other.getParam();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRequestUpgradeTaskPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        result = result * 59 + this.getParam();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRequestUpgradeTaskPet(petId=" + this.getPetId() + ", param=" + this.getParam() + ")";
    }
}
