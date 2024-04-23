package com.kitty.system.game.horse.message.vo;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.horse.domain.Horse;
import java.util.ArrayList;
import java.util.List;

public class Mount {
  private String id;
  
  private int serial;
  
  private String name;
  
  private short level;
  
  private int moveSp;
  
  private int growUp;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setMoveSp(int moveSp) {
    this.moveSp = moveSp;
  }
  
  public void setGrowUp(int growUp) {
    this.growUp = growUp;
  }
  
  public void setGrowthDan(byte growthDan) {
    this.growthDan = growthDan;
  }
  
  public void setVit(int vit) {
    this.vit = vit;
  }
  
  public void setExp(String exp) {
    this.exp = exp;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setSkill(List<FiedValue> skill) {
    this.skill = skill;
  }
  
  public String getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public int getMoveSp() {
    return this.moveSp;
  }
  
  public int getGrowUp() {
    return this.growUp;
  }
  
  private byte growthDan = 0;
  
  private int vit;
  
  private String exp;
  
  public byte getGrowthDan() {
    return this.growthDan;
  }
  
  public int getVit() {
    return this.vit;
  }
  
  public String getExp() {
    return this.exp;
  }
  
  private int score = 0;
  
  public int getScore() {
    return this.score;
  }
  
  private byte state = 0;
  
  private List<FiedValue> skill;
  
  public byte getState() {
    return this.state;
  }
  
  public List<FiedValue> getSkill() {
    return this.skill;
  }
  
  public Mount(Horse horse) {
    this.id = String.valueOf(horse.getId());
    this.serial = horse.getSerial();
    this.name = horse.getName();
    this.level = horse.getLevel();
    this.moveSp = horse.getMoveSp();
    this.growUp = horse.getGrowUp();
    this.growthDan = horse.getGrowthDan();
    this.vit = horse.getVit();
    this.exp = String.valueOf(horse.getExp());
    this.score = horse.getScore();
    this.state = horse.getState();
    List<FiedValue> list = new ArrayList<>();
    for (Integer key : horse.getSkill().keySet())
      list.add(new FiedValue(String.valueOf(key), (byte)4, String.valueOf(horse.getSkill().get(key)))); 
    this.skill = list;
  }
  
  public Mount() {}
}
