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
        this.suit_icon = Integer.valueOf(0);
        this.weaponIcon = Integer.valueOf(0);
        this.gem = 0;
        this.masquerade = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.pet = new ArrayList<>();
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
        TeamMember other = (TeamMember)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$roleId = this.getRoleId();
        Object other$roleId = other.getRoleId();
        if (!((this$roleId != null) ? this$roleId.equals(other$roleId) : (other$roleId == null))) {
            return false;
        }
        Object this$suit_icon = this.getSuit_icon();
        Object other$suit_icon = other.getSuit_icon();
        if (!((this$suit_icon != null) ? this$suit_icon.equals(other$suit_icon) : (other$suit_icon == null))) {
            return false;
        }
        Object this$weaponIcon = this.getWeaponIcon();
        Object other$weaponIcon = other.getWeaponIcon();
        if (!((this$weaponIcon != null) ? this$weaponIcon.equals(other$weaponIcon) : (other$weaponIcon == null))) {
            return false;
        }
        if (this.getGem() != other.getGem()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        if (this.getMasquerade() != other.getMasquerade()) {
            return false;
        }
        Object this$light_effect = this.getLight_effect();
        Object other$light_effect = other.getLight_effect();
        if (!((this$light_effect != null) ? this$light_effect.equals(other$light_effect) : (other$light_effect == null))) {
            return false;
        }
        Object this$special_icon = this.getSpecial_icon();
        Object other$special_icon = other.getSpecial_icon();
        if (!((this$special_icon != null) ? this$special_icon.equals(other$special_icon) : (other$special_icon == null))) {
            return false;
        }
        Object this$pet = this.getPet();
        Object other$pet = other.getPet();
        return ((this$pet != null) ? this$pet.equals(other$pet) : (other$pet == null)) && this.getStatus() == other.getStatus();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TeamMember;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        Object $suit_icon = this.getSuit_icon();
        result = result * 59 + (($suit_icon == null) ? 43 : $suit_icon.hashCode());
        Object $weaponIcon = this.getWeaponIcon();
        result = result * 59 + (($weaponIcon == null) ? 43 : $weaponIcon.hashCode());
        result = result * 59 + this.getGem();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getMasquerade();
        Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        Object $pet = this.getPet();
        result = result * 59 + (($pet == null) ? 43 : $pet.hashCode());
        result = result * 59 + this.getStatus();
        return result;
    }
    
    @Override
    public String toString() {
        return "TeamMember(roleId=" + this.getRoleId() + ", suit_icon=" + this.getSuit_icon() + ", weaponIcon=" + this.getWeaponIcon() + ", gem=" + (int)this.getGem() + ", list=" + this.getList() + ", masquerade=" + this.getMasquerade() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", pet=" + this.getPet() + ", status=" + this.getStatus() + ")";
    }
}
