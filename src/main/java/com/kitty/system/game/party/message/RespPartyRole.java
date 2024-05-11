
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
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
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
        final RespPartyRole other = (RespPartyRole)o;
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
        final Object this$light_effect = this.getLight_effect();
        final Object other$light_effect = other.getLight_effect();
        Label_0104: {
            if (this$light_effect == null) {
                if (other$light_effect == null) {
                    break Label_0104;
                }
            }
            else if (this$light_effect.equals(other$light_effect)) {
                break Label_0104;
            }
            return false;
        }
        final Object this$special_icon = this.getSpecial_icon();
        final Object other$special_icon = other.getSpecial_icon();
        Label_0141: {
            if (this$special_icon == null) {
                if (other$special_icon == null) {
                    break Label_0141;
                }
            }
            else if (this$special_icon.equals(other$special_icon)) {
                break Label_0141;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return this.getState() == other.getState();
            }
        }
        else if (this$list.equals(other$list)) {
            return this.getState() == other.getState();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartyRole;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getWeaponIcon();
        result = result * 59 + this.getGem();
        final Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        final Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyRole(roleId=" + this.getRoleId() + ", weaponIcon=" + this.getWeaponIcon() + ", gem=" + this.getGem() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", list=" + this.getList() + ", state=" + this.getState() + ")";
    }
}
