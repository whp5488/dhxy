
package com.kitty.system.game.team.domain;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

public class TeamMember
{
    private Integer roleId;
    private Integer suit_icon;
    private Integer weaponIcon;
    private byte gem;
    private List<FiedValue> list;
    private int masquerade;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> pet;
    private int status;
    
    public TeamMember() {
        this.suit_icon = 0;
        this.weaponIcon = 0;
        this.gem = 0;
        this.masquerade = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.pet = new ArrayList<FiedValue>();
        this.status = 1;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public Integer getSuit_icon() {
        return this.suit_icon;
    }
    
    public Integer getWeaponIcon() {
        return this.weaponIcon;
    }
    
    public byte getGem() {
        return this.gem;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public int getMasquerade() {
        return this.masquerade;
    }
    
    public List<Integer> getLight_effect() {
        return this.light_effect;
    }
    
    public List<Integer> getSpecial_icon() {
        return this.special_icon;
    }
    
    public List<FiedValue> getPet() {
        return this.pet;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setSuit_icon(final Integer suit_icon) {
        this.suit_icon = suit_icon;
    }
    
    public void setWeaponIcon(final Integer weaponIcon) {
        this.weaponIcon = weaponIcon;
    }
    
    public void setGem(final byte gem) {
        this.gem = gem;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setMasquerade(final int masquerade) {
        this.masquerade = masquerade;
    }
    
    public void setLight_effect(final List<Integer> light_effect) {
        this.light_effect = light_effect;
    }
    
    public void setSpecial_icon(final List<Integer> special_icon) {
        this.special_icon = special_icon;
    }
    
    public void setPet(final List<FiedValue> pet) {
        this.pet = pet;
    }
    
    public void setStatus(final int status) {
        this.status = status;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TeamMember)) {
            return false;
        }
        final TeamMember other = (TeamMember)o;
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
        final Object this$suit_icon = this.getSuit_icon();
        final Object other$suit_icon = other.getSuit_icon();
        Label_0102: {
            if (this$suit_icon == null) {
                if (other$suit_icon == null) {
                    break Label_0102;
                }
            }
            else if (this$suit_icon.equals(other$suit_icon)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$weaponIcon = this.getWeaponIcon();
        final Object other$weaponIcon = other.getWeaponIcon();
        Label_0139: {
            if (this$weaponIcon == null) {
                if (other$weaponIcon == null) {
                    break Label_0139;
                }
            }
            else if (this$weaponIcon.equals(other$weaponIcon)) {
                break Label_0139;
            }
            return false;
        }
        if (this.getGem() != other.getGem()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0189: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0189;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0189;
            }
            return false;
        }
        if (this.getMasquerade() != other.getMasquerade()) {
            return false;
        }
        final Object this$light_effect = this.getLight_effect();
        final Object other$light_effect = other.getLight_effect();
        Label_0239: {
            if (this$light_effect == null) {
                if (other$light_effect == null) {
                    break Label_0239;
                }
            }
            else if (this$light_effect.equals(other$light_effect)) {
                break Label_0239;
            }
            return false;
        }
        final Object this$special_icon = this.getSpecial_icon();
        final Object other$special_icon = other.getSpecial_icon();
        Label_0276: {
            if (this$special_icon == null) {
                if (other$special_icon == null) {
                    break Label_0276;
                }
            }
            else if (this$special_icon.equals(other$special_icon)) {
                break Label_0276;
            }
            return false;
        }
        final Object this$pet = this.getPet();
        final Object other$pet = other.getPet();
        if (this$pet == null) {
            if (other$pet == null) {
                return this.getStatus() == other.getStatus();
            }
        }
        else if (this$pet.equals(other$pet)) {
            return this.getStatus() == other.getStatus();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TeamMember;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        final Object $suit_icon = this.getSuit_icon();
        result = result * 59 + (($suit_icon == null) ? 43 : $suit_icon.hashCode());
        final Object $weaponIcon = this.getWeaponIcon();
        result = result * 59 + (($weaponIcon == null) ? 43 : $weaponIcon.hashCode());
        result = result * 59 + this.getGem();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getMasquerade();
        final Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        final Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        final Object $pet = this.getPet();
        result = result * 59 + (($pet == null) ? 43 : $pet.hashCode());
        result = result * 59 + this.getStatus();
        return result;
    }
    
    @Override
    public String toString() {
        return "TeamMember(roleId=" + this.getRoleId() + ", suit_icon=" + this.getSuit_icon() + ", weaponIcon=" + this.getWeaponIcon() + ", gem=" + this.getGem() + ", list=" + this.getList() + ", masquerade=" + this.getMasquerade() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", pet=" + this.getPet() + ", status=" + this.getStatus() + ")";
    }
}
