
package com.kitty.system.game.pet.message;

import java.beans.ConstructorProperties;
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
        final RespUpgradeTaskPet other = (RespUpgradeTaskPet)o;
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
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespUpgradeTaskPet;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpgradeTaskPet(petId=" + this.getPetId() + ", list=" + this.getList() + ")";
    }
    
    @ConstructorProperties({ "petId", "list" })
    public RespUpgradeTaskPet(final Integer petId, final List<PetBase> list) {
        this.petId = petId;
        this.list = list;
    }
    
    public RespUpgradeTaskPet() {
    }
}
