package com.kitty.system.init.domain.vo;

import java.io.Serializable;

public class SkillParameter implements Serializable {
  private int serial;
  
  private String name;
  
  private byte fightType;
  
  private byte priority;
  
  public int getSerial() {
    return this.serial;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public byte getFightType() {
    return this.fightType;
  }
  
  public void setFightType(byte fightType) {
    this.fightType = fightType;
  }
  
  public byte getPriority() {
    return this.priority;
  }
  
  public void setPriority(byte priority) {
    this.priority = priority;
  }
}
