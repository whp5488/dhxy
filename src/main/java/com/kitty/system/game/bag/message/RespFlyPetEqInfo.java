
package com.kitty.system.game.bag.message;

import java.beans.ConstructorProperties;
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
        final RespFlyPetEqInfo other = (RespFlyPetEqInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$equip = this.getEquip();
        final Object other$equip = other.getEquip();
        if (this$equip == null) {
            if (other$equip == null) {
                return true;
            }
        }
        else if (this$equip.equals(other$equip)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFlyPetEqInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFlyPetEqInfo(equip=" + this.getEquip() + ")";
    }
    
    @ConstructorProperties({ "equip" })
    public RespFlyPetEqInfo(final PetEquipEasy equip) {
        this.equip = equip;
    }
    
    public RespFlyPetEqInfo() {
    }
}
