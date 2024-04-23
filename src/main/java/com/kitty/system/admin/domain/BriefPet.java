package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefPet implements Serializable {
  private int id;
  
  private int serial;
  
  private int rei;
  
  private int grade;
  
  private int hp;
  
  private int mp;
  
  private int ap;
  
  private int sp;
  
  private int growUp = 0;
  
  private long close = 0L;
  
  private short awaken = 0;
  
  private String skills;
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public int getRei() {
    return this.rei;
  }
  
  public void setRei(int rei) {
    this.rei = rei;
  }
  
  public int getGrade() {
    return this.grade;
  }
  
  public void setGrade(int grade) {
    this.grade = grade;
  }
  
  public int getHp() {
    return this.hp;
  }
  
  public void setHp(int hp) {
    this.hp = hp;
  }
  
  public int getMp() {
    return this.mp;
  }
  
  public void setMp(int mp) {
    this.mp = mp;
  }
  
  public int getAp() {
    return this.ap;
  }
  
  public void setAp(int ap) {
    this.ap = ap;
  }
  
  public int getSp() {
    return this.sp;
  }
  
  public void setSp(int sp) {
    this.sp = sp;
  }
  
  public int getGrowUp() {
    return this.growUp;
  }
  
  public void setGrowUp(int growUp) {
    this.growUp = growUp;
  }
  
  public long getClose() {
    return this.close;
  }
  
  public void setClose(long close) {
    this.close = close;
  }
  
  public short getAwaken() {
    return this.awaken;
  }
  
  public void setAwaken(short awaken) {
    this.awaken = awaken;
  }
  
  public String getSkills() {
    return this.skills;
  }
  
  public void setSkills(String skills) {
    this.skills = skills;
  }
}
