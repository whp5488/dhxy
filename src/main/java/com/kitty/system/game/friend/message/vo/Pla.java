package com.kitty.system.game.friend.message.vo;

import java.beans.ConstructorProperties;

public class Pla {
  private int roleId;
  
  private String name;
  
  private byte rei;
  
  private short level;
  
  private int serial;
  
  private byte status;
  
  private String close;
  
  private String groupId;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  public void setClose(String close) {
    this.close = close;
  }
  
  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  @ConstructorProperties({"roleId", "name", "rei", "level", "serial", "status", "close", "groupId"})
  public Pla(int roleId, String name, byte rei, short level, int serial, byte status, String close, String groupId) {
    this.roleId = roleId;
    this.name = name;
    this.rei = rei;
    this.level = level;
    this.serial = serial;
    this.status = status;
    this.close = close;
    this.groupId = groupId;
  }
  
  public Pla() {}
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getStatus() {
    return this.status;
  }
  
  public String getClose() {
    return this.close;
  }
  
  public String getGroupId() {
    return this.groupId;
  }
}
