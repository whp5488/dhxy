package com.kitty.system.game.party.model;

import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

public class PartyInfo {
  private int serial;
  
  private String name;
  
  private byte level;
  
  private short members;
  
  private int active;
  
  private String aim;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setLevel(byte level) {
    this.level = level;
  }
  
  public void setMembers(short members) {
    this.members = members;
  }
  
  public void setActive(int active) {
    this.active = active;
  }
  
  public void setAim(String aim) {
    this.aim = aim;
  }
  
  public void setSum(short sum) {
    this.sum = sum;
  }
  
  public void setWin(short win) {
    this.win = win;
  }
  
  public void setLose(short lose) {
    this.lose = lose;
  }
  
  public void setStandings(short standings) {
    this.standings = standings;
  }
  
  public void setMaster(List<FiedValue> master) {
    this.master = master;
  }
  
  public void setFlag(List<FiedValue> flag) {
    this.flag = flag;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public byte getLevel() {
    return this.level;
  }
  
  public short getMembers() {
    return this.members;
  }
  
  public int getActive() {
    return this.active;
  }
  
  public String getAim() {
    return this.aim;
  }
  
  private short sum = 0;
  
  public short getSum() {
    return this.sum;
  }
  
  private short win = 0;
  
  public short getWin() {
    return this.win;
  }
  
  private short lose = 0;
  
  public short getLose() {
    return this.lose;
  }
  
  private short standings = 0;
  
  public short getStandings() {
    return this.standings;
  }
  
  private List<FiedValue> master = new ArrayList<>();
  
  public List<FiedValue> getMaster() {
    return this.master;
  }
  
  private List<FiedValue> flag = new ArrayList<>();
  
  public List<FiedValue> getFlag() {
    return this.flag;
  }
}
