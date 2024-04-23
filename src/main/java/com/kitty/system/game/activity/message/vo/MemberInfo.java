package com.kitty.system.game.activity.message.vo;

public class MemberInfo {
  private String name;
  
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
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public String getName() {
    return this.name;
  }
  
  private byte rei = 0;
  
  private short level;
  
  private int serial;
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  private int score = 0;
  
  public int getScore() {
    return this.score;
  }
  
  public MemberInfo(String name, byte rei, short level, int serial, int score) {
    this.name = name;
    this.rei = rei;
    this.level = level;
    this.serial = serial;
    this.score = score;
  }
  
  public MemberInfo() {}
}
