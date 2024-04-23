package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45244)
public class RespAtGuessRaceInfo extends Message {
  private int startTime;
  
  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }
  
  public void setMateSecond(int mateSecond) {
    this.mateSecond = mateSecond;
  }
  
  public int getStartTime() {
    return this.startTime;
  }
  
  private int mateSecond = 0;
  
  public int getMateSecond() {
    return this.mateSecond;
  }
}
