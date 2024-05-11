
package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4221)
public class ReqClassifyPetRanking extends Message
{
    private int petSerial;
    private byte serverId;
    
    public int getPetSerial() {
        return this.petSerial;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public void setPetSerial(final int petSerial) {
        this.petSerial = petSerial;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqClassifyPetRanking)) {
            return false;
        }
        final ReqClassifyPetRanking other = (ReqClassifyPetRanking)o;
        return other.canEqual(this) && this.getPetSerial() == other.getPetSerial() && this.getServerId() == other.getServerId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqClassifyPetRanking;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetSerial();
        result = result * 59 + this.getServerId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqClassifyPetRanking(petSerial=" + this.getPetSerial() + ", serverId=" + this.getServerId() + ")";
    }
}
