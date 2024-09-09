package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65507)
public class RespPetInfo extends Message
{
    private Integer roleId;
    private Integer petId;
    private String petName;
    private Integer petType;
    private String color;
    private Integer combat;
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public String getPetName() {
        return this.petName;
    }
    
    public Integer getPetType() {
        return this.petType;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public Integer getCombat() {
        return this.combat;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setPetName(final String petName) {
        this.petName = petName;
    }
    
    public void setPetType(final Integer petType) {
        this.petType = petType;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    public void setCombat(final Integer combat) {
        this.combat = combat;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPetInfo)) {
            return false;
        }
        RespPetInfo other = (RespPetInfo)o;
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
        Object this$petName = this.getPetName();
        Object other$petName = other.getPetName();
        if (!((this$petName != null) ? this$petName.equals(other$petName) : (other$petName == null))) {
            return false;
        }
        Object this$petType = this.getPetType();
        Object other$petType = other.getPetType();
        if (!((this$petType != null) ? this$petType.equals(other$petType) : (other$petType == null))) {
            return false;
        }
        Object this$color = this.getColor();
        Object other$color = other.getColor();
        if (!((this$color != null) ? this$color.equals(other$color) : (other$color == null))) {
            return false;
        }
        Object this$combat = this.getCombat();
        Object other$combat = other.getCombat();
        return (this$combat != null) ? this$combat.equals(other$combat) : (other$combat == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        Object $petName = this.getPetName();
        result = result * 59 + (($petName == null) ? 43 : $petName.hashCode());
        Object $petType = this.getPetType();
        result = result * 59 + (($petType == null) ? 43 : $petType.hashCode());
        Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        Object $combat = this.getCombat();
        result = result * 59 + (($combat == null) ? 43 : $combat.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetInfo(roleId=" + this.getRoleId() + ", petId=" + this.getPetId() + ", petName=" + this.getPetName() + ", petType=" + this.getPetType() + ", color=" + this.getColor() + ", combat=" + this.getCombat() + ")";
    }
    
    public RespPetInfo(final Integer roleId, final Integer petId, final String petName, final Integer petType, final String color, final Integer combat) {
        this.combat = Integer.valueOf(0);
        this.roleId = roleId;
        this.petId = petId;
        this.petName = petName;
        this.petType = petType;
        this.color = color;
        this.combat = combat;
    }
    
    public RespPetInfo() {
        this.combat = Integer.valueOf(0);
    }
}
