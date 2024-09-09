package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45094)
public class ReqPetFlyStarter extends Message
{
    private int petId;
    private byte starter;
    
    public ReqPetFlyStarter() {
    }
    
    public int getPetId() {
        return this.petId;
    }
    
    public byte getStarter() {
        return this.starter;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setStarter(final byte starter) {
        this.starter = starter;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPetFlyStarter)) {
            return false;
        }
        ReqPetFlyStarter other = (ReqPetFlyStarter)o;
        return other.canEqual(this) && this.getPetId() == other.getPetId() && this.getStarter() == other.getStarter();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPetFlyStarter;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        result = result * 59 + this.getStarter();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPetFlyStarter(petId=" + this.getPetId() + ", starter=" + (int)this.getStarter() + ")";
    }
}
