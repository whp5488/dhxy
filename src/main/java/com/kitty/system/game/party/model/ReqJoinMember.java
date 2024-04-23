package com.kitty.system.game.party.model;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class ReqJoinMember {
  private int id;
  
  private String name;
  
  private int time;
  
  private byte rei;
  
  private short level;
  
  private short serial;
  
  private List<Integer> light_effect;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setSerial(short serial) {
    this.serial = serial;
  }
  
  public void setLight_effect(List<Integer> light_effect) {
    this.light_effect = light_effect;
  }
  
  public ReqJoinMember() {
    this.light_effect = new ArrayList<>();
  }
  
  @ConstructorProperties({"id", "name", "time", "rei", "level", "serial", "light_effect"})
  public ReqJoinMember(int id, String name, int time, byte rei, short level, short serial, List<Integer> light_effect) {
    this.light_effect = new ArrayList<>();
    this.id = id;
    this.name = name;
    this.time = time;
    this.rei = rei;
    this.level = level;
    this.serial = serial;
    this.light_effect = light_effect;
  }
  
  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public short getSerial() {
    return this.serial;
  }
  
  public List<Integer> getLight_effect() {
    return this.light_effect;
  }
}
