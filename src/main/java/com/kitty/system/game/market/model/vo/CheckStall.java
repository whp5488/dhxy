
package com.kitty.system.game.market.model.vo;

import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.pet.domain.Pet;

public class CheckStall
{
    private Pet pet;
    private BagEquip equip;
    private RoleTianFu rune;
    
    public CheckStall() {
    }
    
    public CheckStall(final Pet pet) {
        this.pet = pet;
    }
    
    public CheckStall(final BagEquip equip) {
        this.equip = equip;
    }
    
    public CheckStall(final RoleTianFu rune) {
        this.rune = rune;
    }
    
    public Pet getPet() {
        return this.pet;
    }
    
    public BagEquip getEquip() {
        return this.equip;
    }
    
    public RoleTianFu getRune() {
        return this.rune;
    }
    
    public void setPet(final Pet pet) {
        this.pet = pet;
    }
    
    public void setEquip(final BagEquip equip) {
        this.equip = equip;
    }
    
    public void setRune(final RoleTianFu rune) {
        this.rune = rune;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CheckStall)) {
            return false;
        }
        final CheckStall other = (CheckStall)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$pet = this.getPet();
        final Object other$pet = other.getPet();
        Label_0065: {
            if (this$pet == null) {
                if (other$pet == null) {
                    break Label_0065;
                }
            }
            else if (this$pet.equals(other$pet)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$equip = this.getEquip();
        final Object other$equip = other.getEquip();
        Label_0102: {
            if (this$equip == null) {
                if (other$equip == null) {
                    break Label_0102;
                }
            }
            else if (this$equip.equals(other$equip)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$rune = this.getRune();
        final Object other$rune = other.getRune();
        if (this$rune == null) {
            if (other$rune == null) {
                return true;
            }
        }
        else if (this$rune.equals(other$rune)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CheckStall;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $pet = this.getPet();
        result = result * 59 + (($pet == null) ? 43 : $pet.hashCode());
        final Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        final Object $rune = this.getRune();
        result = result * 59 + (($rune == null) ? 43 : $rune.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CheckStall(pet=" + this.getPet() + ", equip=" + this.getEquip() + ", rune=" + this.getRune() + ")";
    }
}
