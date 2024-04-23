package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49166)
public class RespGangWarsMatchInfo extends Message {
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setMax(short max) {
    this.max = max;
  }
  
  public void setSideName(String sideName) {
    this.sideName = sideName;
  }
  
  public void setSideNum(short sideNum) {
    this.sideNum = sideNum;
  }
  
  public void setSideGateLife(short sideGateLife) {
    this.sideGateLife = sideGateLife;
  }
  
  public void setSideWinRonda(short sideWinRonda) {
    this.sideWinRonda = sideWinRonda;
  }
  
  public void setSideContestWinRonda(short sideContestWinRonda) {
    this.sideContestWinRonda = sideContestWinRonda;
  }
  
  public void setSidePylonRonda(short sidePylonRonda) {
    this.sidePylonRonda = sidePylonRonda;
  }
  
  public void setSideCannonRonda(short sideCannonRonda) {
    this.sideCannonRonda = sideCannonRonda;
  }
  
  public void setEnemyName(String enemyName) {
    this.enemyName = enemyName;
  }
  
  public void setEnemyNum(short enemyNum) {
    this.enemyNum = enemyNum;
  }
  
  public void setEnemyGateLife(short enemyGateLife) {
    this.enemyGateLife = enemyGateLife;
  }
  
  public void setEnemyWinRonda(short enemyWinRonda) {
    this.enemyWinRonda = enemyWinRonda;
  }
  
  public void setEnemyContestWinRonda(short enemyContestWinRonda) {
    this.enemyContestWinRonda = enemyContestWinRonda;
  }
  
  public void setEnemyPylonRonda(short enemyPylonRonda) {
    this.enemyPylonRonda = enemyPylonRonda;
  }
  
  public void setEnemyCannonRonda(short enemyCannonRonda) {
    this.enemyCannonRonda = enemyCannonRonda;
  }
  
  private byte type = 2;
  
  public byte getType() {
    return this.type;
  }
  
  private short max = 5000;
  
  private String sideName;
  
  public short getMax() {
    return this.max;
  }
  
  public String getSideName() {
    return this.sideName;
  }
  
  private short sideNum = 0;
  
  public short getSideNum() {
    return this.sideNum;
  }
  
  private short sideGateLife = 0;
  
  public short getSideGateLife() {
    return this.sideGateLife;
  }
  
  private short sideWinRonda = 0;
  
  public short getSideWinRonda() {
    return this.sideWinRonda;
  }
  
  private short sideContestWinRonda = 0;
  
  public short getSideContestWinRonda() {
    return this.sideContestWinRonda;
  }
  
  private short sidePylonRonda = 0;
  
  public short getSidePylonRonda() {
    return this.sidePylonRonda;
  }
  
  private short sideCannonRonda = 0;
  
  private String enemyName;
  
  public short getSideCannonRonda() {
    return this.sideCannonRonda;
  }
  
  public String getEnemyName() {
    return this.enemyName;
  }
  
  private short enemyNum = 0;
  
  public short getEnemyNum() {
    return this.enemyNum;
  }
  
  private short enemyGateLife = 0;
  
  public short getEnemyGateLife() {
    return this.enemyGateLife;
  }
  
  private short enemyWinRonda = 0;
  
  public short getEnemyWinRonda() {
    return this.enemyWinRonda;
  }
  
  private short enemyContestWinRonda = 0;
  
  public short getEnemyContestWinRonda() {
    return this.enemyContestWinRonda;
  }
  
  private short enemyPylonRonda = 0;
  
  public short getEnemyPylonRonda() {
    return this.enemyPylonRonda;
  }
  
  private short enemyCannonRonda = 0;
  
  public short getEnemyCannonRonda() {
    return this.enemyCannonRonda;
  }
}
