package com.kitty.system.game.pet.message;

import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12025)
public class RespPetEffctInfo extends Message
{
    private Integer roleId;
    private Integer petId;
    private List<PetSkill> list;
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public List<PetSkill> getList() {
        return this.list;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setList(final List<PetSkill> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPetEffctInfo)) {
            return false;
        }
        RespPetEffctInfo other = (RespPetEffctInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$roleId = this.getRoleId();
        Object other$roleId = other.getRoleId();
        if (!((this$roleId != null) ? this$roleId.equals(other$roleId) : (other$roleId == null))) {
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
        return other instanceof RespPetEffctInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetEffctInfo(roleId=" + this.getRoleId() + ", petId=" + this.getPetId() + ", list=" + this.getList() + ")";
    }
    
    public RespPetEffctInfo(final Integer roleId, final Integer petId, final List<PetSkill> list) {
        this.roleId = roleId;
        this.petId = petId;
        this.list = list;
    }
    
    public RespPetEffctInfo() {
    }
}
