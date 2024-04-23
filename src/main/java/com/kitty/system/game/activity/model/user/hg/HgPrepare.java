package com.kitty.system.game.activity.model.user.hg;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class HgPrepare implements Serializable {
  private int serial;
  
  private byte rei;
  
  private short grade;
  
  private String name;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setGrade(short grade) {
    this.grade = grade;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  @ConstructorProperties({"serial", "rei", "grade", "name", "state"})
  public HgPrepare(int serial, byte rei, short grade, String name, byte state) {
    this.serial = serial;
    this.rei = rei;
    this.grade = grade;
    this.name = name;
    this.state = state;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getGrade() {
    return this.grade;
  }
  
  public String getName() {
    return this.name;
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  public HgPrepare() {}
}
