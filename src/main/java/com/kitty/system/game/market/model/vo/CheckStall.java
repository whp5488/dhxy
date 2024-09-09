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
    
    public CheckStall(Pet pet) {
        this.pet = pet;
    }
    
    public CheckStall(BagEquip equip) {
        this.equip = equip;
    }
    
    public CheckStall(RoleTianFu rune) {
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
        CheckStall other = (CheckStall)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$pet = this.getPet();
        Object other$pet = other.getPet();
        if (!((this$pet != null) ? this$pet.equals(other$pet) : (other$pet == null))) {
            return false;
        }
        Object this$equip = this.getEquip();
        Object other$equip = other.getEquip();
        if (!((this$equip != null) ? this$equip.equals(other$equip) : (other$equip == null))) {
            return false;
        }
        Object this$rune = this.getRune();
        Object other$rune = other.getRune();
        return (this$rune != null) ? this$rune.equals(other$rune) : (other$rune == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CheckStall;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $pet = this.getPet();
        result = result * 59 + (($pet == null) ? 43 : $pet.hashCode());
        Object $equip = this.getEquip();
        result = result * 59 + (($equip == null) ? 43 : $equip.hashCode());
        Object $rune = this.getRune();
        result = result * 59 + (($rune == null) ? 43 : $rune.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CheckStall(pet=" + this.getPet() + ", equip=" + this.getEquip() + ", rune=" + this.getRune() + ")";
    }
}
