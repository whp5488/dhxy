package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42035)
public class RespElseFight extends Message {
  public void setAutoFight(int autoFight) {
    this.autoFight = autoFight;
  }
  
  public void setShowDbSpeed(int showDbSpeed) {
    this.showDbSpeed = showDbSpeed;
  }
  
  private int autoFight = 0;
  
  public int getAutoFight() {
    return this.autoFight;
  }
  
  private int showDbSpeed = 0;
  
  public int getShowDbSpeed() {
    return this.showDbSpeed;
  }
}
