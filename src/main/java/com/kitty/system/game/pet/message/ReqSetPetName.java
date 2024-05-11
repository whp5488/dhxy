
package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8272)
public class ReqSetPetName extends Message
{
    private Integer petId;
    private String name;
    
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
        final ReqSetPetName other = (ReqSetPetName)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$petId = this.getPetId();
        final Object other$petId = other.getPetId();
        Label_0065: {
            if (this$petId == null) {
                if (other$petId == null) {
                    break Label_0065;
                }
            }
            else if (this$petId.equals(other$petId)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return true;
            }
        }
        else if (this$name.equals(other$name)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSetPetName;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSetPetName(petId=" + this.getPetId() + ", name=" + this.getName() + ")";
    }
}
