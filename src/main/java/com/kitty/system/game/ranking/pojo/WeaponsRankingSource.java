package com.kitty.system.game.ranking.pojo;

import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class WeaponsRankingSource implements Serializable
{
    private Long wearEquipId;
    private int roleId;
    private String roleName;
    private int serial;
    private byte race;
    private int weaponsSerial;
    private String weaponsName;
    private int score;
    private byte equipType;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public WeaponsRankingSource(Long wearEquipId) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
        this.wearEquipId = wearEquipId;
    }
    
    public WeaponsRankingSource(WearEquip wearEquip, Role role, int score) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
        this.wearEquipId = wearEquip.getId();
        this.roleId = (int)role.getRoleId();
        this.weaponsSerial = wearEquip.getType();
        this.weaponsName = wearEquip.getName();
        this.score = score;
        this.equipType = (byte)wearEquip.getPosition();
        this.rests(role);
    }
    
    public void rests(Role role) {
        this.race = (byte)(int)role.getRace();
        this.roleName = role.getName();
        this.serial = (int)role.getSerial();
        EquipService equipService = SpringUtils.getEquipService();
        this.weapon = role.getWeaponIcon();
        this.light_effect = equipService.getWearEffect(role);
        List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
        }
        this.list = SpringUtils.getRoleService().deyList(role.getColor());
    }
    
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof WeaponsRankingSource)) {
            WeaponsRankingSource weaponsRankingSource = (WeaponsRankingSource)obj;
            return this.wearEquipId.equals(weaponsRankingSource.getWearEquipId());
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = this.wearEquipId.hashCode() * prime;
        return result;
    }
    
    public Long getWearEquipId() {
        return this.wearEquipId;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public int getWeaponsSerial() {
        return this.weaponsSerial;
    }
    
    public String getWeaponsName() {
        return this.weaponsName;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public byte getEquipType() {
        return this.equipType;
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
    
    public void setWearEquipId(final Long wearEquipId) {
        this.wearEquipId = wearEquipId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setWeaponsSerial(final int weaponsSerial) {
        this.weaponsSerial = weaponsSerial;
    }
    
    public void setWeaponsName(final String weaponsName) {
        this.weaponsName = weaponsName;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setEquipType(final byte equipType) {
        this.equipType = equipType;
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
    
    public WeaponsRankingSource(final Long wearEquipId, final int roleId, final String roleName, final int serial, final byte race, final int weaponsSerial, final String weaponsName, final int score, final byte equipType, final int weapon, final int gem, final List<Integer> light_effect, final List<Integer> special_icon, final List<FiedValue> list) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
        this.wearEquipId = wearEquipId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.serial = serial;
        this.race = race;
        this.weaponsSerial = weaponsSerial;
        this.weaponsName = weaponsName;
        this.score = score;
        this.equipType = equipType;
        this.weapon = weapon;
        this.gem = gem;
        this.light_effect = light_effect;
        this.special_icon = special_icon;
        this.list = list;
    }
    
    public WeaponsRankingSource() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
    }
}
