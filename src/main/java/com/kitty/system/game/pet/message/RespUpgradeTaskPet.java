package com.kitty.system.game.pet.message;

import com.kitty.system.game.pet.domain.PetBase;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45315)
public class RespUpgradeTaskPet extends Message
{
    private Integer petId;
    private List<PetBase> list;
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public List<PetBase> getList() {
        return this.list;
    }
    
    public void setPetId(final Integer petId) {
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
        if (!(o instanceof RespUpgradeTaskPet)) {
            return false;
        }
        RespUpgradeTaskPet other = (RespUpgradeTaskPet)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$petId = this.getPetId();
        Object other$petId = other.getPetId();
        if (!((this$petId != null) ? this$petId.equals(other$petId) : (other$petId == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespUpgradeTaskPet;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpgradeTaskPet(petId=" + this.getPetId() + ", list=" + this.getList() + ")";
    }
    
    public RespUpgradeTaskPet(final Integer petId, final List<PetBase> list) {
        this.petId = petId;
        this.list = list;
    }
    
    public RespUpgradeTaskPet() {
    }
}
