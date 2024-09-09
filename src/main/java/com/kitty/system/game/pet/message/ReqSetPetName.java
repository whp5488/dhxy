package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8272)
public class ReqSetPetName extends Message
{
    private Integer petId;
    private String name;
    
    public ReqSetPetName() {
    }
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSetPetName)) {
            return false;
        }
        ReqSetPetName other = (ReqSetPetName)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$petId = this.getPetId();
        Object other$petId = other.getPetId();
        if (!((this$petId != null) ? this$petId.equals(other$petId) : (other$petId == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return (this$name != null) ? this$name.equals(other$name) : (other$name == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSetPetName;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSetPetName(petId=" + this.getPetId() + ", name=" + this.getName() + ")";
    }
}
