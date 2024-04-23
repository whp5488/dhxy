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

public class QlkRankingSource implements Serializable {
  private int roleId;
  
  private String roleName;
  
  private int race;
  
  private int serial;
  
  private int qlkj;
  
  private int qlkm;
  
  private int qlks;
  
  private int qlkh;
  
  private int qlkt;
  
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
  
  public void setQlkj(int qlkj) {
    this.qlkj = qlkj;
  }
  
  public void setQlkm(int qlkm) {
    this.qlkm = qlkm;
  }
  
  public void setQlks(int qlks) {
    this.qlks = qlks;
  }
  
  public void setQlkh(int qlkh) {
    this.qlkh = qlkh;
  }
  
  public void setQlkt(int qlkt) {
    this.qlkt = qlkt;
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
  
  @ConstructorProperties({"roleId", "roleName", "race", "serial", "qlkj", "qlkm", "qlks", "qlkh", "qlkt", "weapon", "gem", "light_effect", "special_icon", "list"})
  public QlkRankingSource(int roleId, String roleName, int race, int serial, int qlkj, int qlkm, int qlks, int qlkh, int qlkt, int weapon, int gem, List<Integer> light_effect, List<Integer> special_icon, List<FiedValue> list) {
    this.roleId = roleId;
    this.roleName = roleName;
    this.race = race;
    this.serial = serial;
    this.qlkj = qlkj;
    this.qlkm = qlkm;
    this.qlks = qlks;
    this.qlkh = qlkh;
    this.qlkt = qlkt;
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
  
  public int getQlkj() {
    return this.qlkj;
  }
  
  public int getQlkm() {
    return this.qlkm;
  }
  
  public int getQlks() {
    return this.qlks;
  }
  
  public int getQlkh() {
    return this.qlkh;
  }
  
  public int getQlkt() {
    return this.qlkt;
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
  
  public QlkRankingSource(int roleId) {
    this.roleId = roleId;
  }
  
  public QlkRankingSource(Role role, int[] qlkArrays) {
    this.roleId = role.getRoleId().intValue();
    this.qlkj = qlkArrays[0];
    this.qlkm = qlkArrays[1];
    this.qlks = qlkArrays[2];
    this.qlkh = qlkArrays[3];
    this.qlkt = qlkArrays[4];
    rests(role);
  }
  
  public void rests(Role role) {
    this.roleName = role.getName();
    this.serial = role.getSerial().intValue();
    this.race = role.getRace().intValue();
    EquipService equipService = SpringUtils.getEquipService();
    this.weapon = role.getWeaponIcon();
    this.light_effect = equipService.getWearEffect(role);
    List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
    if (wearEquips.size() > 0)
      this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips); 
    this.list = SpringUtils.getRoleService().deyList(role.getColor());
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof QlkRankingSource) {
      QlkRankingSource qlkRankingSource = (QlkRankingSource)obj;
      if (this.roleId == qlkRankingSource.getRoleId())
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
  
  public QlkRankingSource() {}
}
