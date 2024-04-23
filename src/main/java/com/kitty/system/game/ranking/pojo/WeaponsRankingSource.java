package com.kitty.system.game.ranking.pojo;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WeaponsRankingSource implements Serializable {
  private Long wearEquipId;
  
  private int roleId;
  
  private String roleName;
  
  private int serial;
  
  private byte race;
  
  private int weaponsSerial;
  
  private String weaponsName;
  
  private int score;
  
  private byte equipType;
  
  public void setWearEquipId(Long wearEquipId) {
    this.wearEquipId = wearEquipId;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRace(byte race) {
    this.race = race;
  }
  
  public void setWeaponsSerial(int weaponsSerial) {
    this.weaponsSerial = weaponsSerial;
  }
  
  public void setWeaponsName(String weaponsName) {
    this.weaponsName = weaponsName;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setEquipType(byte equipType) {
    this.equipType = equipType;
  }
  
  public void setWeapon(int weapon) {
    this.weapon = weapon;
  }
  
  public void setGem(int gem) {
    this.gem = gem;
  }
  
  public void setLight_effect(List<Integer> light_effect) {
    this.light_effect = light_effect;
  }
  
  public void setSpecial_icon(List<Integer> special_icon) {
    this.special_icon = special_icon;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  @ConstructorProperties({"wearEquipId", "roleId", "roleName", "serial", "race", "weaponsSerial", "weaponsName", "score", "equipType", "weapon", "gem", "light_effect", "special_icon", "list"})
  public WeaponsRankingSource(Long wearEquipId, int roleId, String roleName, int serial, byte race, int weaponsSerial, String weaponsName, int score, byte equipType, int weapon, int gem, List<Integer> light_effect, List<Integer> special_icon, List<FiedValue> list) {
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
  
  private int weapon = 0;
  
  public int getWeapon() {
    return this.weapon;
  }
  
  private int gem = 0;
  
  public int getGem() {
    return this.gem;
  }
  
  private List<Integer> light_effect = new ArrayList<>();
  
  public List<Integer> getLight_effect() {
    return this.light_effect;
  }
  
  private List<Integer> special_icon = new ArrayList<>();
  
  public List<Integer> getSpecial_icon() {
    return this.special_icon;
  }
  
  private List<FiedValue> list = new ArrayList<>();
  
  public List<FiedValue> getList() {
    return this.list;
  }
  
  public WeaponsRankingSource(Long wearEquipId) {
    this.wearEquipId = wearEquipId;
  }
  
  public WeaponsRankingSource(WearEquip wearEquip, Role role, int score) {
    this.wearEquipId = wearEquip.getId();
    this.roleId = role.getRoleId().intValue();
    this.weaponsSerial = wearEquip.getType();
    this.weaponsName = wearEquip.getName();
    this.score = score;
    this.equipType = (byte)wearEquip.getPosition();
    rests(role);
  }
  
  public void rests(Role role) {
    this.race = (byte)role.getRace().intValue();
    this.roleName = role.getName();
    this.serial = role.getSerial().intValue();
    EquipService equipService = SpringUtils.getEquipService();
    this.weapon = role.getWeaponIcon();
    this.light_effect = equipService.getWearEffect(role);
    List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
    if (wearEquips.size() > 0)
      this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips); 
    this.list = SpringUtils.getRoleService().deyList(role.getColor());
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof WeaponsRankingSource) {
      WeaponsRankingSource weaponsRankingSource = (WeaponsRankingSource)obj;
      if (this.wearEquipId.equals(weaponsRankingSource.getWearEquipId()))
        return true; 
      return false;
    } 
    return false;
  }
  
  public int hashCode() {
    int result = 1;
    int prime = 31;
    result = this.wearEquipId.hashCode() * prime;
    return result;
  }
  
  public WeaponsRankingSource() {}
}
