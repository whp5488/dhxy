package com.kitty.system.game.party.model;

public class JoinMemberInfo {
  private int id;
  
  private String name;
  
  private short serial;
  
  private byte rei;
  
  private short level;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(short serial) {
    this.serial = serial;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public short getSerial() {
    return this.serial;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
}
