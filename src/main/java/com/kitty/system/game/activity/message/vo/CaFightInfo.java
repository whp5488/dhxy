package com.kitty.system.game.activity.message.vo;

import java.io.Serializable;

public class CaFightInfo implements Serializable {
  public void setItselfLeader(int itselfLeader) {
    this.itselfLeader = itselfLeader;
  }
  
  public void setItselfLeaderSerial(int itselfLeaderSerial) {
    this.itselfLeaderSerial = itselfLeaderSerial;
  }
  
  public void setItselfLeaderName(String itselfLeaderName) {
    this.itselfLeaderName = itselfLeaderName;
  }
  
  public void setEnemyLeaderSerial(int enemyLeaderSerial) {
    this.enemyLeaderSerial = enemyLeaderSerial;
  }
  
  public void setEnemyLeaderName(String enemyLeaderName) {
    this.enemyLeaderName = enemyLeaderName;
  }
  
  private int itselfLeader = 0;
  
  public int getItselfLeader() {
    return this.itselfLeader;
  }
  
  private int itselfLeaderSerial = 0;
  
  private String itselfLeaderName;
  
  public int getItselfLeaderSerial() {
    return this.itselfLeaderSerial;
  }
  
  public String getItselfLeaderName() {
    return this.itselfLeaderName;
  }
  
  private int enemyLeaderSerial = 0;
  
  private String enemyLeaderName;
  
  public int getEnemyLeaderSerial() {
    return this.enemyLeaderSerial;
  }
  
  public String getEnemyLeaderName() {
    return this.enemyLeaderName;
  }
}
