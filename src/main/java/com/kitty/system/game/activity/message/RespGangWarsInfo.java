package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49165)
public class RespGangWarsInfo extends Message {
  private String sideName;
  
  public void setSideName(String sideName) {
    this.sideName = sideName;
  }
  
  public void setSideNum(short sideNum) {
    this.sideNum = sideNum;
  }
  
  public void setEnemyName(String enemyName) {
    this.enemyName = enemyName;
  }
  
  public void setEnemyNum(short enemyNum) {
    this.enemyNum = enemyNum;
  }
  
  public String getSideName() {
    return this.sideName;
  }
  
  private short sideNum = 0;
  
  private String enemyName;
  
  public short getSideNum() {
    return this.sideNum;
  }
  
  public String getEnemyName() {
    return this.enemyName;
  }
  
  private short enemyNum = 0;
  
  public short getEnemyNum() {
    return this.enemyNum;
  }
}
