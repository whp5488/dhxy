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
    
    public void addSkillIds(Pet pet) {
        List<Integer> skillIds = FightOtherUtil.addSkillIds(pet);
        if (skillIds.size() > 0) {
            this.skillIds = skillIds;
        }
    }
    
    public CombatAddObject() {
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
        CombatAddObject other = (CombatAddObject)o;
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
        Object this$effects = this.getEffects();
        Object other$effects = other.getEffects();
        if (!((this$effects != null) ? this$effects.equals(other$effects) : (other$effects == null))) {
            return false;
        }
        Object this$suit_light_effects = this.getSuit_light_effects();
        Object other$suit_light_effects = other.getSuit_light_effects();
        if (!((this$suit_light_effects != null) ? this$suit_light_effects.equals(other$suit_light_effects) : (other$suit_light_effects == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        Object this$skillIds = this.getSkillIds();
        Object other$skillIds = other.getSkillIds();
        return (this$skillIds != null) ? this$skillIds.equals(other$skillIds) : (other$skillIds == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof CombatAddObject;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getHostId();
        result = result * 59 + this.getLeader();
        result = result * 59 + this.getPosition();
        result = result * 59 + this.getCustomIcon();
        result = result * 59 + this.getWeapon_icon();
        Object $effects = this.getEffects();
        result = result * 59 + (($effects == null) ? 43 : $effects.hashCode());
        Object $suit_light_effects = this.getSuit_light_effects();
        result = result * 59 + (($suit_light_effects == null) ? 43 : $suit_light_effects.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        Object $skillIds = this.getSkillIds();
        result = result * 59 + (($skillIds == null) ? 43 : $skillIds.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "CombatAddObject(id=" + this.getId() + ", serial=" + this.getSerial() + ", hostId=" + this.getHostId() + ", leader=" + this.getLeader() + ", position=" + this.getPosition() + ", customIcon=" + this.getCustomIcon() + ", weapon_icon=" + this.getWeapon_icon() + ", effects=" + this.getEffects() + ", suit_light_effects=" + this.getSuit_light_effects() + ", list=" + this.getList() + ", skillIds=" + this.getSkillIds() + ")";
    }
}
