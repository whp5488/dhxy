
package com.kitty.system.game.ranking.pojo.field;

import com.kitty.system.game.ranking.pojo.PetRankingSource;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.ranking.pojo.RankingField;

public class PetPropertyRankingField extends RankingField
{
    private int petId;
    private String roleName;
    private int petSerial;
    private int propertyValue;
    private int roleId;
    private int serial;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public PetPropertyRankingField() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
    }
    
    public PetPropertyRankingField(final PetRankingSource obj, final int index, final byte serverId, final int propertyValue) {
        super(index, serverId);
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.petId = obj.getPetId();
        this.roleId = obj.getRoleId();
        this.roleName = obj.getRoleName();
        this.petSerial = obj.getPetSerial();
        this.propertyValue = propertyValue;
        this.serial = obj.getSerial();
        this.weapon = obj.getWeapon();
        this.gem = obj.getGem();
        this.light_effect = obj.getLight_effect();
        this.special_icon = obj.getSpecial_icon();
        this.list = obj.getList();
    }
    
    public int getPetId() {
        return this.petId;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public int getPetSerial() {
        return this.petSerial;
    }
    
    public int getPropertyValue() {
        return this.propertyValue;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getWeapon() {
        return this.weapon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public List<Integer> getLight_effect() {
        return this.light_effect;
    }
    
    public List<Integer> getSpecial_icon() {
        return this.special_icon;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setPetSerial(final int petSerial) {
        this.petSerial = petSerial;
    }
    
    public void setPropertyValue(final int propertyValue) {
        this.propertyValue = propertyValue;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setWeapon(final int weapon) {
        this.weapon = weapon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setLight_effect(final List<Integer> light_effect) {
        this.light_effect = light_effect;
    }
    
    public void setSpecial_icon(final List<Integer> special_icon) {
        this.special_icon = special_icon;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PetPropertyRankingField)) {
            return false;
        }
        final PetPropertyRankingField other = (PetPropertyRankingField)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getPetId() != other.getPetId()) {
            return false;
        }
        final Object this$roleName = this.getRoleName();
        final Object other$roleName = other.getRoleName();
        Label_0078: {
            if (this$roleName == null) {
                if (other$roleName == null) {
                    break Label_0078;
                }
            }
            else if (this$roleName.equals(other$roleName)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getPetSerial() != other.getPetSerial()) {
            return false;
        }
        if (this.getPropertyValue() != other.getPropertyValue()) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getWeapon() != other.getWeapon()) {
            return false;
        }
        if (this.getGem() != other.getGem()) {
            return false;
        }
        final Object this$light_effect = this.getLight_effect();
        final Object other$light_effect = other.getLight_effect();
        Label_0193: {
            if (this$light_effect == null) {
                if (other$light_effect == null) {
                    break Label_0193;
                }
            }
            else if (this$light_effect.equals(other$light_effect)) {
                break Label_0193;
            }
            return false;
        }
        final Object this$special_icon = this.getSpecial_icon();
        final Object other$special_icon = other.getSpecial_icon();
        Label_0230: {
            if (this$special_icon == null) {
                if (other$special_icon == null) {
                    break Label_0230;
                }
            }
            else if (this$special_icon.equals(other$special_icon)) {
                break Label_0230;
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
        return other instanceof PetPropertyRankingField;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPetId();
        final Object $roleName = this.getRoleName();
        result = result * 59 + (($roleName == null) ? 43 : $roleName.hashCode());
        result = result * 59 + this.getPetSerial();
        result = result * 59 + this.getPropertyValue();
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getWeapon();
        result = result * 59 + this.getGem();
        final Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        final Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PetPropertyRankingField(petId=" + this.getPetId() + ", roleName=" + this.getRoleName() + ", petSerial=" + this.getPetSerial() + ", propertyValue=" + this.getPropertyValue() + ", roleId=" + this.getRoleId() + ", serial=" + this.getSerial() + ", weapon=" + this.getWeapon() + ", gem=" + this.getGem() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", list=" + this.getList() + ")";
    }
}
