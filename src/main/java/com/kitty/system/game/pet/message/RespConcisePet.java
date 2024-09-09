package com.kitty.system.game.pet.message;

import com.kitty.system.game.pet.domain.PetBase;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41154)
public class RespConcisePet extends Message
{
    private int petId;
    private List<PetBase> list;
    
    public int getPetId() {
        return this.petId;
    }
    
    public List<PetBase> getList() {
        return this.list;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setList(final List<PetBase> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespConcisePet)) {
            return false;
        }
        RespConcisePet other = (RespConcisePet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPetId() != other.getPetId()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespConcisePet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespConcisePet(petId=" + this.getPetId() + ", list=" + this.getList() + ")";
    }
    
    public RespConcisePet(final int petId, final List<PetBase> list) {
        this.petId = petId;
        this.list = list;
    }
    
    public RespConcisePet() {
    }
}
