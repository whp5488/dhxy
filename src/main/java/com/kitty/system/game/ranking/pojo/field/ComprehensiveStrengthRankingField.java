package com.kitty.system.game.ranking.pojo.field;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import java.util.ArrayList;
import java.util.List;

public class ComprehensiveStrengthRankingField extends RankingField {
  private int roleId;
  
  private String roleName;
  
  private int serial;
  
  private int totalRepair;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
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
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getRoleName() {
    return this.roleName;
  }
  
  public int getSerial() {
    return this.serial;
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
  
  public ComprehensiveStrengthRankingField(ComprehensiveStrengthSource obj, int order, byte serverId) {
    super(order, serverId);
    this.roleId = obj.getRoleId();
    this.roleName = obj.getRoleName();
    this.serial = obj.getSerial();
    this.totalRepair = obj.getTotalRepair();
    this.weapon = obj.getWeapon();
    this.light_effect = obj.getLight_effect();
    this.gem = obj.getGem();
    this.list = obj.getList();
  }
  
  public ComprehensiveStrengthRankingField() {}
}
