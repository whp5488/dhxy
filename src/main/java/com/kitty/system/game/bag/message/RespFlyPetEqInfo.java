package com.kitty.system.game.bag.message;

import com.kitty.system.game.equip.domain.PetEquipEasy;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65523)
public class RespFlyPetEqInfo extends Message
{
    private PetEquipEasy equip;
    
    public PetEquipEasy getEquip() {
        return this.equip;
    }
    
    public void setEquip(final PetEquipEasy equip) {
        this.equip = equip;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFlyPetEqInfo)) {
            return false;
        }
        RespFlyPetEqInfo other = (RespFlyPetEqInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$equip = this.getEquip();
        Object other$equip = other.getEquip();
        return (this$equip != null) ? this$equip.equals(other$equip) : (other$equip == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFlyPetEqInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFlyPetEqInfo(equip=" + this.getEquip() + ")";
    }
    
    public RespFlyPetEqInfo(final PetEquipEasy equip) {
        this.equip = equip;
    }
    
    public RespFlyPetEqInfo() {
    }
}
