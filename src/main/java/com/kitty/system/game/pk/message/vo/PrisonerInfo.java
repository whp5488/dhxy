package com.kitty.system.game.pk.message.vo;

import com.kitty.system.game.role.domain.Role;

public class PrisonerInfo {
  private int roleId;
  
  private String name;
  
  private int serial;
  
  private byte rei;
  
  private short level;
  
  private int lastTi;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setLastTi(int lastTi) {
    this.lastTi = lastTi;
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
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public int getLastTi() {
    return this.lastTi;
  }
  
  public PrisonerInfo() {}
  
  public PrisonerInfo(Role role) {
    this.roleId = role.getRoleId().intValue();
    this.name = role.getName();
    this.serial = role.getSerial().intValue();
    this.rei = role.getRelive().byteValue();
    this.level = role.getLevel().shortValue();
    this.lastTi = role.getPkInfoBox().getPrisonTime();
  }
  
  public PrisonerInfo(int roleId, String name, int serial, byte rei, short level, int lastTi) {
    this.roleId = roleId;
    this.name = name;
    this.serial = serial;
    this.rei = rei;
    this.level = level;
    this.lastTi = lastTi;
  }
}
