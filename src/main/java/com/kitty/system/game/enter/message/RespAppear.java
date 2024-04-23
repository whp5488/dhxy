package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 65529)
public class RespAppear extends Message {
  private int roleId;
  
  private int posX;
  
  private int posY;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setPosX(int posX) {
    this.posX = posX;
  }
  
  public void setPosY(int posY) {
    this.posY = posY;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPartyName(String partyName) {
    this.partyName = partyName;
  }
  
  public void setJob(byte job) {
    this.job = job;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRelive(int relive) {
    this.relive = relive;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public void setTitle(int title) {
    this.title = title;
  }
  
  public void setLeaderId(int leaderId) {
    this.leaderId = leaderId;
  }
  
  public void setLight_effect(List<Integer> light_effect) {
    this.light_effect = light_effect;
  }
  
  public void setSpecial_icon(List<Integer> special_icon) {
    this.special_icon = special_icon;
  }
  
  public void setMount_icon(int mount_icon) {
    this.mount_icon = mount_icon;
  }
  
  public void setVip(int vip) {
    this.vip = vip;
  }
  
  public void setMoveSpeed(int moveSpeed) {
    this.moveSpeed = moveSpeed;
  }
  
  public void setFly_type(int fly_type) {
    this.fly_type = fly_type;
  }
  
  public void setPortrait(int portrait) {
    this.portrait = portrait;
  }
  
  public void setWeapon(int weapon) {
    this.weapon = weapon;
  }
  
  public void setGem(int gem) {
    this.gem = gem;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public int getPosX() {
    return this.posX;
  }
  
  public int getPosY() {
    return this.posY;
  }
  
  private int serial = 1;
  
  public int getSerial() {
    return this.serial;
  }
  
  private String partyName = "";
  
  public String getPartyName() {
    return this.partyName;
  }
  
  private byte job = 0;
  
  private String name;
  
  public byte getJob() {
    return this.job;
  }
  
  public String getName() {
    return this.name;
  }
  
  private int relive = 0;
  
  public int getRelive() {
    return this.relive;
  }
  
  private int level = 0;
  
  private int title;
  
  public int getLevel() {
    return this.level;
  }
  
  public int getTitle() {
    return this.title;
  }
  
  private int leaderId = 0;
  
  public int getLeaderId() {
    return this.leaderId;
  }
  
  private List<Integer> light_effect = new ArrayList<>();
  
  public List<Integer> getLight_effect() {
    return this.light_effect;
  }
  
  private List<Integer> special_icon = new ArrayList<>();
  
  public List<Integer> getSpecial_icon() {
    return this.special_icon;
  }
  
  private int mount_icon = 0;
  
  public int getMount_icon() {
    return this.mount_icon;
  }
  
  private int vip = 0;
  
  public int getVip() {
    return this.vip;
  }
  
  private int moveSpeed = 0;
  
  public int getMoveSpeed() {
    return this.moveSpeed;
  }
  
  private int fly_type = 0;
  
  public int getFly_type() {
    return this.fly_type;
  }
  
  private int portrait = 0;
  
  public int getPortrait() {
    return this.portrait;
  }
  
  private int weapon = 0;
  
  public int getWeapon() {
    return this.weapon;
  }
  
  private int gem = 0;
  
  private List<FiedValue> list;
  
  public int getGem() {
    return this.gem;
  }
  
  public List<FiedValue> getList() {
    return this.list;
  }
}
