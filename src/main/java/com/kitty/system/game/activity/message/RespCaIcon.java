package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49180)
public class RespCaIcon extends Message {
  public void setRound(byte round) {
    this.round = round;
  }
  
  public void setCurrentSession(byte currentSession) {
    this.currentSession = currentSession;
  }
  
  public void setTotalSession(byte totalSession) {
    this.totalSession = totalSession;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  private byte round = 1;
  
  public byte getRound() {
    return this.round;
  }
  
  private byte currentSession = 1;
  
  public byte getCurrentSession() {
    return this.currentSession;
  }
  
  private byte totalSession = 3;
  
  private byte state;
  
  public byte getTotalSession() {
    return this.totalSession;
  }
  
  public byte getState() {
    return this.state;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
}
