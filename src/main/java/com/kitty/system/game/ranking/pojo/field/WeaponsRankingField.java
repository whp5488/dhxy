package com.kitty.system.game.ranking.pojo.field;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import java.util.ArrayList;
import java.util.List;

public class WeaponsRankingField extends RankingField {
  private String wearEquipId;
  
  private int roleId;
  
  private String roleName;
  
  private int serial;
  
  private int weaponsSerial;
  
  private String weaponsName;
  
  private int score;
  
  public void setWearEquipId(String wearEquipId) {
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
  
  public void setWeaponsSerial(int weaponsSerial) {
    this.weaponsSerial = weaponsSerial;
  }
  
  public void setWeaponsName(String weaponsName) {
    this.weaponsName = weaponsName;
  }
  
  public void setScore(int score) {
    this.score = score;
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
  
  public WeaponsRankingField(WeaponsRankingSource obj, int order, byte serverId) {
    super(order, serverId);
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
  
  public WeaponsRankingField() {}
}
