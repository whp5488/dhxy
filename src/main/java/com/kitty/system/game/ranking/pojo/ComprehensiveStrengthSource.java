
package com.kitty.system.game.ranking.pojo;

import java.beans.ConstructorProperties;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class ComprehensiveStrengthSource implements Serializable
{
    private int roleId;
    private String roleName;
    private int race;
    private int serial;
    private int skillScore;
    private int petScore;
    private int wearEquipScore;
    private int totalRepair;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public ComprehensiveStrengthSource(final int roleId) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = roleId;
    }
    
    public ComprehensiveStrengthSource(final Role role, final Map<String, Integer> map) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = role.getRoleId();
        this.roleName = role.getName();
        this.race = role.getRace();
        this.serial = role.getSerial();
        this.skillScore = map.get("skillScore");
        this.petScore = map.get("petScore");
        this.wearEquipScore = map.get("wearEquipScore");
        this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
        this.rests(role);
    }
    
    public void rests(final Role role) {
        final EquipService equipService = SpringUtils.getEquipService();
        this.weapon = role.getWeaponIcon();
        this.light_effect = equipService.getWearEffect(role);
        final List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
        }
        this.list = SpringUtils.getRoleService().deyList(role.getColor());
    }
    
    public void setSkillScore(final int skillScore) {
        this.skillScore = skillScore;
        this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
    }
    
    public void setPetScore(final int petScore) {
        this.petScore = petScore;
        this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
    }
    
    public void setWearEquipScore(final int wearEquipScore) {
        this.wearEquipScore = wearEquipScore;
        this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ComprehensiveStrengthSource) {
            final ComprehensiveStrengthSource comprehensiveStrengthSource = (ComprehensiveStrengthSource)obj;
            return this.roleId == comprehensiveStrengthSource.getRoleId();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        final int prime = 31;
        result = this.getRoleId() * prime;
        return result;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getSkillScore() {
        return this.skillScore;
    }
    
    public int getPetScore() {
        return this.petScore;
    }
    
    public int getWearEquipScore() {
        return this.wearEquipScore;
    }
    
    public int getTotalRepair() {
        return this.totalRepair;
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
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTotalRepair(final int totalRepair) {
        this.totalRepair = totalRepair;
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
    
    @ConstructorProperties({ "roleId", "roleName", "race", "serial", "skillScore", "petScore", "wearEquipScore", "totalRepair", "weapon", "gem", "light_effect", "special_icon", "list" })
    public ComprehensiveStrengthSource(final int roleId, final String roleName, final int race, final int serial, final int skillScore, final int petScore, final int wearEquipScore, final int totalRepair, final int weapon, final int gem, final List<Integer> light_effect, final List<Integer> special_icon, final List<FiedValue> list) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = roleId;
        this.roleName = roleName;
        this.race = race;
        this.serial = serial;
        this.skillScore = skillScore;
        this.petScore = petScore;
        this.wearEquipScore = wearEquipScore;
        this.totalRepair = totalRepair;
        this.weapon = weapon;
        this.gem = gem;
        this.light_effect = light_effect;
        this.special_icon = special_icon;
        this.list = list;
    }
    
    public ComprehensiveStrengthSource() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
    }
}
