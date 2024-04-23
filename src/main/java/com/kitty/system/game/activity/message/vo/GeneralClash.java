package com.kitty.system.game.activity.message.vo;

import java.beans.ConstructorProperties;

public class GeneralClash {
  private String name;
  
  private int icon;
  
  private byte rei;
  
  private short level;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setIcon(int icon) {
    this.icon = icon;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  @ConstructorProperties({"name", "icon", "rei", "level"})
  public GeneralClash(String name, int icon, byte rei, short level) {
    this.name = name;
    this.icon = icon;
    this.rei = rei;
    this.level = level;
  }
  
  public GeneralClash() {}
  
  public String getName() {
    return this.name;
  }
  
  public int getIcon() {
    return this.icon;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
}
