package com.kitty.system.game.activity.model.user.wars.vo;

import java.io.Serializable;

public class GangWarsInfo implements Serializable {
  public void setGateLife(short gateLife) {
    this.gateLife = gateLife;
  }
  
  public void setStance(byte stance) {
    this.stance = stance;
  }
  
  public void setVisible(int visible) {
    this.visible = visible;
  }
  
  public void setGrade(int grade) {
    this.grade = grade;
  }
  
  public void setEnemy(int enemy) {
    this.enemy = enemy;
  }
  
  public void setCombatResults(byte combatResults) {
    this.combatResults = combatResults;
  }
  
  private short gateLife = 5000;
  
  private byte stance;
  
  private int visible;
  
  public short getGateLife() {
    return this.gateLife;
  }
  
  public byte getStance() {
    return this.stance;
  }
  
  public int getVisible() {
    return this.visible;
  }
  
  private int grade = 1;
  
  private int enemy;
  
  public int getGrade() {
    return this.grade;
  }
  
  public int getEnemy() {
    return this.enemy;
  }
  
  private byte combatResults = 3;
  
  public byte getCombatResults() {
    return this.combatResults;
  }
  
  public GangWarsInfo(byte stance, int visible, int grade, int enemy) {
    this.stance = stance;
    this.visible = visible;
    this.grade = grade;
    this.enemy = enemy;
  }
  
  public GangWarsInfo(short gateLife, byte stance, int visible, int grade, int enemy) {
    this.gateLife = gateLife;
    this.stance = stance;
    this.visible = visible;
    this.grade = grade;
    this.enemy = enemy;
  }
  
  public GangWarsInfo() {}
}
