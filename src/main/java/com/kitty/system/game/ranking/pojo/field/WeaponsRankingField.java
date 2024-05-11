
package com.kitty.system.game.ranking.pojo.field;

import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.ranking.pojo.RankingField;

public class WeaponsRankingField extends RankingField
{
    private String wearEquipId;
    private int roleId;
    private String roleName;
    private int serial;
    private int weaponsSerial;
    private String weaponsName;
    private int score;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public WeaponsRankingField() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
    }
    
    public WeaponsRankingField(final WeaponsRankingSource obj, final int order, final byte serverId) {
        super(order, serverId);
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.wearEquipId = String.valueOf(obj.getWearEquipId());
        this.roleId = obj.getRoleId();
        this.roleName = obj.getRoleName();
        this.serial = obj.getSerial();
        this.weaponsSerial = obj.getWeaponsSerial();
        this.weaponsName = obj.getWeaponsName();
        this.score = obj.getScore();
        this.weapon = obj.getWeapon();
        this.gem = obj.getGem();
        this.light_effect = obj.getLight_effect();
        this.special_icon = obj.getSpecial_icon();
        this.list = obj.getList();
    }
    
    public String getWearEquipId() {
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
    
    public int getWeaponsSerial() {
        return this.weaponsSerial;
    }
    
    public String getWeaponsName() {
        return this.weaponsName;
    }
    
    public int getScore() {
        return this.score;
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
    
    public void setWearEquipId(final String wearEquipId) {
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
    
    public void setWeaponsSerial(final int weaponsSerial) {
        this.weaponsSerial = weaponsSerial;
    }
    
    public void setWeaponsName(final String weaponsName) {
        this.weaponsName = weaponsName;
    }
    
    public void setScore(final int score) {
        this.score = score;
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
}
