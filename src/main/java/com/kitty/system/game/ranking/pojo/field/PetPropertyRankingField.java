package com.kitty.system.game.ranking.pojo.field;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.ranking.pojo.PetRankingSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import java.util.ArrayList;
import java.util.List;

public class PetPropertyRankingField extends RankingField {
  private int petId;
  
  private String roleName;
  
  private int petSerial;
  
  private int propertyValue;
  
  private int roleId;
  
  private int serial;
  
  public void setPetId(int petId) {
    this.petId = petId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  public void setPetSerial(int petSerial) {
    this.petSerial = petSerial;
  }
  
  public void setPropertyValue(int propertyValue) {
    this.propertyValue = propertyValue;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
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
  
  public int getPetId() {
    return this.petId;
  }
  
  public String getRoleName() {
    return this.roleName;
  }
  
  public int getPetSerial() {
    return this.petSerial;
  }
  
  public int getPropertyValue() {
    return this.propertyValue;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getSerial() {
    return this.serial;
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
  
  public PetPropertyRankingField(PetRankingSource obj, int index, byte serverId, int propertyValue) {
    super(index, serverId);
    this.petId = obj.getPetId();
    this.roleId = obj.getRoleId();
    this.roleName = obj.getRoleName();
    this.petSerial = obj.getPetSerial();
    this.propertyValue = propertyValue;
    this.serial = obj.getSerial();
    this.weapon = obj.getWeapon();
    this.gem = obj.getGem();
    this.light_effect = obj.getLight_effect();
    this.special_icon = obj.getSpecial_icon();
    this.list = obj.getList();
  }
  
  public PetPropertyRankingField() {}
}
