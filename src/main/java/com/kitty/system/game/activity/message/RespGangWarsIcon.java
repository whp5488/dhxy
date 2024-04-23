package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49164)
public class RespGangWarsIcon extends Message {
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setMax(short max) {
    this.max = max;
  }
  
  public void setSideName(String sideName) {
    this.sideName = sideName;
  }
  
  public void setSideLife(short sideLife) {
    this.sideLife = sideLife;
  }
  
  public void setSideGrade(byte sideGrade) {
    this.sideGrade = sideGrade;
  }
  
  public void setEnemyName(String enemyName) {
    this.enemyName = enemyName;
  }
  
  public void setEnemyLife(short enemyLife) {
    this.enemyLife = enemyLife;
  }
  
  public void setEnemyGrade(byte enemyGrade) {
    this.enemyGrade = enemyGrade;
  }
  
  public void setStance(byte stance) {
    this.stance = stance;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  private short max = 5000;
  
  private String sideName;
  
  public short getMax() {
    return this.max;
  }
  
  public String getSideName() {
    return this.sideName;
  }
  
  private short sideLife = 0;
  
  public short getSideLife() {
    return this.sideLife;
  }
  
  private byte sideGrade = 0;
  
  private String enemyName;
  
  public byte getSideGrade() {
    return this.sideGrade;
  }
  
  public String getEnemyName() {
    return this.enemyName;
  }
  
  private short enemyLife = 0;
  
  public short getEnemyLife() {
    return this.enemyLife;
  }
  
  private byte enemyGrade = 0;
  
  private byte stance;
  
  public byte getEnemyGrade() {
    return this.enemyGrade;
  }
  
  public byte getStance() {
    return this.stance;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
}
