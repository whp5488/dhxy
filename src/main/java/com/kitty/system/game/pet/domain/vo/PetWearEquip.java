
package com.kitty.system.game.pet.domain.vo;

import com.kitty.system.game.equip.domain.WearEquip;
import java.util.List;
import java.io.Serializable;

public class PetWearEquip implements Serializable
{
    List<WearEquip> list;
    
    public List<WearEquip> getList() {
        return this.list;
    }
    
    public void setList(final List<WearEquip> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetWearEquip)) {
            return false;
        }
        final PetWearEquip other = (PetWearEquip)o;
        if (!other.canEqual(this)) {
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
        return other instanceof PetWearEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PetWearEquip(list=" + this.getList() + ")";
    }
}
