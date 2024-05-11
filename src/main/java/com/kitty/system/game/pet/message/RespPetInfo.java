
package com.kitty.system.game.pet.message;

import java.beans.ConstructorProperties;
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
        final RespPetInfo other = (RespPetInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$roleId = this.getRoleId();
        final Object other$roleId = other.getRoleId();
        Label_0065: {
            if (this$roleId == null) {
                if (other$roleId == null) {
                    break Label_0065;
                }
            }
            else if (this$roleId.equals(other$roleId)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$petId = this.getPetId();
        final Object other$petId = other.getPetId();
        Label_0102: {
            if (this$petId == null) {
                if (other$petId == null) {
                    break Label_0102;
                }
            }
            else if (this$petId.equals(other$petId)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$petName = this.getPetName();
        final Object other$petName = other.getPetName();
        Label_0139: {
            if (this$petName == null) {
                if (other$petName == null) {
                    break Label_0139;
                }
            }
            else if (this$petName.equals(other$petName)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$petType = this.getPetType();
        final Object other$petType = other.getPetType();
        Label_0176: {
            if (this$petType == null) {
                if (other$petType == null) {
                    break Label_0176;
                }
            }
            else if (this$petType.equals(other$petType)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        Label_0213: {
            if (this$color == null) {
                if (other$color == null) {
                    break Label_0213;
                }
            }
            else if (this$color.equals(other$color)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$combat = this.getCombat();
        final Object other$combat = other.getCombat();
        if (this$combat == null) {
            if (other$combat == null) {
                return true;
            }
        }
        else if (this$combat.equals(other$combat)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        final Object $petId = this.getPetId();
        result = result * 59 + (($petId == null) ? 43 : $petId.hashCode());
        final Object $petName = this.getPetName();
        result = result * 59 + (($petName == null) ? 43 : $petName.hashCode());
        final Object $petType = this.getPetType();
        result = result * 59 + (($petType == null) ? 43 : $petType.hashCode());
        final Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        final Object $combat = this.getCombat();
        result = result * 59 + (($combat == null) ? 43 : $combat.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetInfo(roleId=" + this.getRoleId() + ", petId=" + this.getPetId() + ", petName=" + this.getPetName() + ", petType=" + this.getPetType() + ", color=" + this.getColor() + ", combat=" + this.getCombat() + ")";
    }
    
    @ConstructorProperties({ "roleId", "petId", "petName", "petType", "color", "combat" })
    public RespPetInfo(final Integer roleId, final Integer petId, final String petName, final Integer petType, final String color, final Integer combat) {
        this.combat = 0;
        this.roleId = roleId;
        this.petId = petId;
        this.petName = petName;
        this.petType = petType;
        this.color = color;
        this.combat = combat;
    }
    
    public RespPetInfo() {
        this.combat = 0;
    }
}
