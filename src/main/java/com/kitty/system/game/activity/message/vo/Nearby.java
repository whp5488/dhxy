package com.kitty.system.game.activity.message.vo;

import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;

public class Nearby implements Serializable {
  private int id;
  
  private int serial;
  
  private String name;
  
  private byte rei;
  
  private short level;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
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
  
  public int getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
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
  
  public Nearby() {}
  
  public Nearby(Role role) {
    this.id = role.getRoleId().intValue();
    this.serial = role.getSerial().intValue();
    this.name = role.getName();
    this.rei = role.getRelive().byteValue();
    this.level = role.getLevel().shortValue();
  }
  
  public Nearby(int id, int serial, String name, byte rei, short level) {
    this.id = id;
    this.serial = serial;
    this.name = name;
    this.rei = rei;
    this.level = level;
  }
}
