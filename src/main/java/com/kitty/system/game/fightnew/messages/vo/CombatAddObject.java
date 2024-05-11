
package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.game.fightnew.utils.FightOtherUtil;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class CombatAddObject implements Serializable
{
    private int id;
    private int serial;
    private int hostId;
    private int leader;
    private int position;
    private int customIcon;
    private int weapon_icon;
    private List<Integer> effects;
    private List<Integer> suit_light_effects;
    private List<FiedValue> list;
    private List<Integer> skillIds;
    
    public void addSkillIds(final Pet pet) {
        final List<Integer> skillIds = FightOtherUtil.addSkillIds(pet);
        if (skillIds.size() > 0) {
            this.skillIds = skillIds;
        }
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getHostId() {
        return this.hostId;
    }
    
    public int getLeader() {
        return this.leader;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public int getCustomIcon() {
        return this.customIcon;
    }
    
    public int getWeapon_icon() {
        return this.weapon_icon;
    }
    
    public List<Integer> getEffects() {
        return this.effects;
    }
    
    public List<Integer> getSuit_light_effects() {
        return this.suit_light_effects;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public List<Integer> getSkillIds() {
        return this.skillIds;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setHostId(final int hostId) {
        this.hostId = hostId;
    }
    
    public void setLeader(final int leader) {
        this.leader = leader;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setCustomIcon(final int customIcon) {
        this.customIcon = customIcon;
    }
    
    public void setWeapon_icon(final int weapon_icon) {
        this.weapon_icon = weapon_icon;
    }
    
    public void setEffects(final List<Integer> effects) {
        this.effects = effects;
    }
    
    public void setSuit_light_effects(final List<Integer> suit_light_effects) {
        this.suit_light_effects = suit_light_effects;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setSkillIds(final List<Integer> skillIds) {
        this.skillIds = skillIds;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CombatAddObject)) {
            return false;
        }
        final CombatAddObject other = (CombatAddObject)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getHostId() != other.getHostId()) {
            return false;
        }
        if (this.getLeader() != other.getLeader()) {
            return false;
        }
        if (this.getPosition() != other.getPosition()) {
            return false;
        }
        if (this.getCustomIcon() != other.getCustomIcon()) {
            return false;
        }
        if (this.getWeapon_icon() != other.getWeapon_icon()) {
            return false;
        }
        final Object this$effects = this.getEffects();
        final Object other$effects = other.getEffects();
        Label_0156: {
            if (this$effects == null) {
                if (other$effects == null) {
                    break Label_0156;
                }
            }
            else if (this$effects.equals(other$effects)) {
                break Label_0156;
            }
            return false;
        }
        final Object this$suit_light_effects = this.getSuit_light_effects();
        final Object other$suit_light_effects = other.getSuit_light_effects();
        Label_0193: {
            if (this$suit_light_effects == null) {
                if (other$suit_light_effects == null) {
                    break Label_0193;
                }
            }
            else if (this$suit_light_effects.equals(other$suit_light_effects)) {
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
        final Object this$skillIds = this.getSkillIds();
        final Object other$skillIds = other.getSkillIds();
        if (this$skillIds == null) {
            if (other$skillIds == null) {
                return true;
            }
        }
        else if (this$skillIds.equals(other$skillIds)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CombatAddObject;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getHostId();
        result = result * 59 + this.getLeader();
        result = result * 59 + this.getPosition();
        result = result * 59 + this.getCustomIcon();
        result = result * 59 + this.getWeapon_icon();
        final Object $effects = this.getEffects();
        result = result * 59 + (($effects == null) ? 43 : $effects.hashCode());
        final Object $suit_light_effects = this.getSuit_light_effects();
        result = result * 59 + (($suit_light_effects == null) ? 43 : $suit_light_effects.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $skillIds = this.getSkillIds();
        result = result * 59 + (($skillIds == null) ? 43 : $skillIds.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CombatAddObject(id=" + this.getId() + ", serial=" + this.getSerial() + ", hostId=" + this.getHostId() + ", leader=" + this.getLeader() + ", position=" + this.getPosition() + ", customIcon=" + this.getCustomIcon() + ", weapon_icon=" + this.getWeapon_icon() + ", effects=" + this.getEffects() + ", suit_light_effects=" + this.getSuit_light_effects() + ", list=" + this.getList() + ", skillIds=" + this.getSkillIds() + ")";
    }
}
