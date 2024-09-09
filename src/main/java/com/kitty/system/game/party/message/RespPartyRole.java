package com.kitty.system.game.party.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8369)
public class RespPartyRole extends Message
{
    private int roleId;
    private int weaponIcon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    private byte state;
    
    public RespPartyRole() {
        this.weaponIcon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getWeaponIcon() {
        return this.weaponIcon;
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
    
    public byte getState() {
        return this.state;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setWeaponIcon(final int weaponIcon) {
        this.weaponIcon = weaponIcon;
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
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPartyRole)) {
            return false;
        }
        RespPartyRole other = (RespPartyRole)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getWeaponIcon() != other.getWeaponIcon()) {
            return false;
        }
        if (this.getGem() != other.getGem()) {
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
        Object this$list = this.getList();
        Object other$list = other.getList();
        return ((this$list != null) ? this$list.equals(other$list) : (other$list == null)) && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartyRole;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getWeaponIcon();
        result = result * 59 + this.getGem();
        Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyRole(roleId=" + this.getRoleId() + ", weaponIcon=" + this.getWeaponIcon() + ", gem=" + this.getGem() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", list=" + this.getList() + ", state=" + (int)this.getState() + ")";
    }
}
