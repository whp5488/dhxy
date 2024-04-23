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
import java.util.Map;

public class ComprehensiveStrengthSource implements Serializable {
  private int roleId;
  
  private String roleName;
  
  private int race;
  
  private int serial;
  
  private int skillScore;
  
  private int petScore;
  
  private int wearEquipScore;
  
  private int totalRepair;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  public void setRace(int race) {
    this.race = race;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setTotalRepair(int totalRepair) {
    this.totalRepair = totalRepair;
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
  
  @ConstructorProperties({"roleId", "roleName", "race", "serial", "skillScore", "petScore", "wearEquipScore", "totalRepair", "weapon", "gem", "light_effect", "special_icon", "list"})
  public ComprehensiveStrengthSource(int roleId, String roleName, int race, int serial, int skillScore, int petScore, int wearEquipScore, int totalRepair, int weapon, int gem, List<Integer> light_effect, List<Integer> special_icon, List<FiedValue> list) {
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
  
  public ComprehensiveStrengthSource(int roleId) {
    this.roleId = roleId;
  }
  
  public ComprehensiveStrengthSource(Role role, Map<String, Integer> map) {
    this.roleId = role.getRoleId().intValue();
    this.roleName = role.getName();
    this.race = role.getRace().intValue();
    this.serial = role.getSerial().intValue();
    this.skillScore = ((Integer)map.get("skillScore")).intValue();
    this.petScore = ((Integer)map.get("petScore")).intValue();
    this.wearEquipScore = ((Integer)map.get("wearEquipScore")).intValue();
    this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
    rests(role);
  }
  
  public void rests(Role role) {
    EquipService equipService = SpringUtils.getEquipService();
    this.weapon = role.getWeaponIcon();
    this.light_effect = equipService.getWearEffect(role);
    List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
    if (wearEquips.size() > 0)
      this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips); 
    this.list = SpringUtils.getRoleService().deyList(role.getColor());
  }
  
  public void setSkillScore(int skillScore) {
    this.skillScore = skillScore;
    this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
  }
  
  public void setPetScore(int petScore) {
    this.petScore = petScore;
    this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
  }
  
  public void setWearEquipScore(int wearEquipScore) {
    this.wearEquipScore = wearEquipScore;
    this.totalRepair = this.skillScore + this.petScore + this.wearEquipScore;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof ComprehensiveStrengthSource) {
      ComprehensiveStrengthSource comprehensiveStrengthSource = (ComprehensiveStrengthSource)obj;
      if (this.roleId == comprehensiveStrengthSource.getRoleId())
        return true; 
      return false;
    } 
    return false;
  }
  
  public int hashCode() {
    int result = 1;
    int prime = 31;
    result = getRoleId() * prime;
    return result;
  }
  
  public ComprehensiveStrengthSource() {}
}
