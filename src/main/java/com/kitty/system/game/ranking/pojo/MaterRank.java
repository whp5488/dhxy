package com.kitty.system.game.ranking.pojo;

import com.kitty.system.common.enter.FiedValue;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MaterRank implements Serializable {
  private int roleId;
  
  private String name;
  
  private int serial;
  
  private byte relive;
  
  private short level;
  
  private int score;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRelive(byte relive) {
    this.relive = relive;
  }
  
  public void setLevel(short level) {
    this.level = level;
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

  @ConstructorProperties({"roleId", "name", "serial", "relive", "level", "score", "weapon", "gem", "light_effect", "special_icon", "list"})
  public MaterRank(int roleId, String name, int serial, byte relive, short level, int score, int weapon, int gem, List<Integer> light_effect, List<Integer> special_icon, List<FiedValue> list) {
    this.roleId = roleId;
    this.name = name;
    this.serial = serial;
    this.relive = relive;
    this.level = level;
    this.score = score;
    this.weapon = weapon;
    this.gem = gem;
    this.light_effect = light_effect;
    this.special_icon = special_icon;
    this.list = list;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getRelive() {
    return this.relive;
  }
  
  public short getLevel() {
    return this.level;
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
  
  public MaterRank() {}
}
