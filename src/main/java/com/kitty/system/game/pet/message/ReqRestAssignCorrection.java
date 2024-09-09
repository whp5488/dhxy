package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14339)
public class ReqRestAssignCorrection extends Message
{
    private Integer petId;
    private byte type;
    
    public ReqRestAssignCorrection() {
    }
    
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
        if (!(o instanceof ReqRestAssignCorrection)) {
            return false;
        }
        ReqRestAssignCorrection other = (ReqRestAssignCorrection)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$petId = this.getPetId();
        Object other$petId = other.getPetId();
        return ((this$petId != null) ? this$petId.equals(other$petId) : (other$petId == null)) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRestAssignCorrection;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRestAssignCorrection(petId=" + this.getPetId() + ", type=" + (int)this.getType() + ")";
    }
}
