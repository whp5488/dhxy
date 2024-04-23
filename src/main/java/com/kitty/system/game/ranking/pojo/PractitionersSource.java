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

public class PractitionersSource implements Serializable {
  private int roleId;
  
  private String roleName;
  
  private int relive;
  
  private int level;
  
  private int reliveAndLevel;
  
  private long exp;
  
  private long nextLevelNeedExp;
  
  private int serial;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  public void setReliveAndLevel(int reliveAndLevel) {
    this.reliveAndLevel = reliveAndLevel;
  }
  
  public void setExp(long exp) {
    this.exp = exp;
  }
  
  public void setNextLevelNeedExp(long nextLevelNeedExp) {
    this.nextLevelNeedExp = nextLevelNeedExp;
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
  
  @ConstructorProperties({"roleId", "roleName", "relive", "level", "reliveAndLevel", "exp", "nextLevelNeedExp", "serial", "weapon", "gem", "light_effect", "special_icon", "list"})
  public PractitionersSource(int roleId, String roleName, int relive, int level, int reliveAndLevel, long exp, long nextLevelNeedExp, int serial, int weapon, int gem, List<Integer> light_effect, List<Integer> special_icon, List<FiedValue> list) {
    this.roleId = roleId;
    this.roleName = roleName;
    this.relive = relive;
    this.level = level;
    this.reliveAndLevel = reliveAndLevel;
    this.exp = exp;
    this.nextLevelNeedExp = nextLevelNeedExp;
    this.serial = serial;
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
  
  public int getRelive() {
    return this.relive;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public int getReliveAndLevel() {
    return this.reliveAndLevel;
  }
  
  public long getExp() {
    return this.exp;
  }
  
  public long getNextLevelNeedExp() {
    return this.nextLevelNeedExp;
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
  
  public PractitionersSource(Role role) {
    this.roleId = role.getRoleId().intValue();
    this.roleName = role.getName();
  }
  
  public PractitionersSource(Role role, long nextLevelNeedExp) {
    this.roleId = role.getRoleId().intValue();
    this.relive = role.getRelive().intValue();
    this.level = role.getLevel().intValue();
    this.reliveAndLevel = this.relive * 1000 + this.level;
    this.exp = role.getExp();
    this.nextLevelNeedExp = nextLevelNeedExp;
    rests(role);
  }
  
  public void rests(Role role) {
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
  
  public void setRelive(int relive) {
    this.relive = relive;
    this.reliveAndLevel = this.relive * 1000 + this.level;
  }
  
  public void setLevel(int level) {
    this.level = level;
    this.reliveAndLevel = this.relive * 1000 + this.level;
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof PractitionersSource) {
      PractitionersSource practitionersSource = (PractitionersSource)obj;
      if (this.roleId == practitionersSource.getRoleId())
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
  
  public PractitionersSource() {}
}
