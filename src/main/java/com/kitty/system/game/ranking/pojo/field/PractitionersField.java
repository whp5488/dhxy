package com.kitty.system.game.ranking.pojo.field;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.pojo.RankingField;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PractitionersField extends RankingField {
  private int roleId;
  
  private String roleName;
  
  private int relive;
  
  private int level;
  
  private String expPercent;
  
  private int serial;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  public void setRelive(int relive) {
    this.relive = relive;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public void setExpPercent(String expPercent) {
    this.expPercent = expPercent;
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
  
  public String getExpPercent() {
    return this.expPercent;
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
  
  public PractitionersField(PractitionersSource obj, int order, byte serverId) {
    super(order, serverId);
    this.roleId = obj.getRoleId();
    this.roleName = obj.getRoleName();
    this.relive = obj.getRelive();
    this.level = obj.getLevel();
    String doubleNum = BigDecimalUtil.divide(String.valueOf(obj.getExp()), String.valueOf(obj.getNextLevelNeedExp()));
    String percent = BigDecimalUtil.multiply(doubleNum, String.valueOf(100));
    BigDecimal bigDecimal = BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), percent);
    this.expPercent = bigDecimal.toString() + "%";
    this.serial = obj.getSerial();
    this.weapon = obj.getWeapon();
    this.gem = obj.getGem();
    this.light_effect = obj.getLight_effect();
    this.special_icon = obj.getSpecial_icon();
    this.list = obj.getList();
  }
  
  public PractitionersField() {}
}
