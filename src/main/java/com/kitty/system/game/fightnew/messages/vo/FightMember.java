
package com.kitty.system.game.fightnew.messages.vo;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class FightMember implements Serializable
{
    private int id;
    private int serial;
    private int host;
    private int leader;
    private int position;
    private int icon;
    private int weaponIcon;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    private List<Integer> statusList;
    
    public FightMember() {
        this.weaponIcon = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getHost() {
        return this.host;
    }
    
    public int getLeader() {
        return this.leader;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public int getWeaponIcon() {
        return this.weaponIcon;
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
    
    public List<Integer> getStatusList() {
        return this.statusList;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setHost(final int host) {
        this.host = host;
    }
    
    public void setLeader(final int leader) {
        this.leader = leader;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setWeaponIcon(final int weaponIcon) {
        this.weaponIcon = weaponIcon;
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
    
    public void setStatusList(final List<Integer> statusList) {
        this.statusList = statusList;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FightMember)) {
            return false;
        }
        final FightMember other = (FightMember)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getHost() != other.getHost()) {
            return false;
        }
        if (this.getLeader() != other.getLeader()) {
            return false;
        }
        if (this.getPosition() != other.getPosition()) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        if (this.getWeaponIcon() != other.getWeaponIcon()) {
            return false;
        }
        final Object this$light_effect = this.getLight_effect();
        final Object other$light_effect = other.getLight_effect();
        Label_0156: {
            if (this$light_effect == null) {
                if (other$light_effect == null) {
                    break Label_0156;
                }
            }
            else if (this$light_effect.equals(other$light_effect)) {
                break Label_0156;
            }
            return false;
        }
        final Object this$special_icon = this.getSpecial_icon();
        final Object other$special_icon = other.getSpecial_icon();
        Label_0193: {
            if (this$special_icon == null) {
                if (other$special_icon == null) {
                    break Label_0193;
                }
            }
            else if (this$special_icon.equals(other$special_icon)) {
                break Label_0193;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0230: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0230;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0230;
            }
            return false;
        }
        final Object this$statusList = this.getStatusList();
        final Object other$statusList = other.getStatusList();
        if (this$statusList == null) {
            if (other$statusList == null) {
                return true;
            }
        }
        else if (this$statusList.equals(other$statusList)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FightMember;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getHost();
        result = result * 59 + this.getLeader();
        result = result * 59 + this.getPosition();
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getWeaponIcon();
        final Object $light_effect = this.getLight_effect();
        result = result * 59 + (($light_effect == null) ? 43 : $light_effect.hashCode());
        final Object $special_icon = this.getSpecial_icon();
        result = result * 59 + (($special_icon == null) ? 43 : $special_icon.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $statusList = this.getStatusList();
        result = result * 59 + (($statusList == null) ? 43 : $statusList.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FightMember(id=" + this.getId() + ", serial=" + this.getSerial() + ", host=" + this.getHost() + ", leader=" + this.getLeader() + ", position=" + this.getPosition() + ", icon=" + this.getIcon() + ", weaponIcon=" + this.getWeaponIcon() + ", light_effect=" + this.getLight_effect() + ", special_icon=" + this.getSpecial_icon() + ", list=" + this.getList() + ", statusList=" + this.getStatusList() + ")";
    }
}
