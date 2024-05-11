
package com.kitty.system.game.equip.helper.vo;

import com.kitty.system.game.bag.domain.BagEquip;

public class EquipInfo
{
    private BagEquip equip;
    private String tips;
    
    public BagEquip getEquip() {
        return this.equip;
    }
    
    public String getTips() {
        return this.tips;
    }
    
    public void setEquip(final BagEquip equip) {
        this.equip = equip;
    }
    
    public void setTips(final String tips) {
        this.tips = tips;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EquipInfo)) {
            return false;
        }
        final EquipInfo other = (EquipInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$equip = this.getEquip();
        final Object other$equip = other.getEquip();
        Label_0065: {
            if (this$equip == null) {
                if (other$equip == null) {
                    break Label_0065;
                }
            }
            else if (this$equip.equals(other$equip)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$tips = this.getTips();
        final Object other$tips = other.getTips();
        if (this$tips == null) {
            if (other$tips == null) {
                return true;
            }
        }
        else if (this$tips.equals(other$tips)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof EquipInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        final Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "EquipInfo(equip=" + this.getEquip() + ", tips=" + this.getTips() + ")";
    }
}
