package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49177)
public class RespActivityPkScore extends Message {
  public void setStage(byte stage) {
    this.stage = stage;
  }
  
  public void setPkValue(short pkValue) {
    this.pkValue = pkValue;
  }
  
  public void setScore(short score) {
    this.score = score;
  }
  
  public void setStartTime(int startTime) {
    this.startTime = startTime;
  }
  
  public void setStageDurationTime(int stageDurationTime) {
    this.stageDurationTime = stageDurationTime;
  }
  
  public void setRank(short rank) {
    this.rank = rank;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  private byte stage = 2;
  
  private short pkValue;
  
  private short score;
  
  private int startTime;
  
  public byte getStage() {
    return this.stage;
  }
  
  public short getPkValue() {
    return this.pkValue;
  }
  
  public short getScore() {
    return this.score;
  }
  
  public int getStartTime() {
    return this.startTime;
  }
  
  private int stageDurationTime = 5400;
  
  private short rank;
  
  private byte type;
  
  public int getStageDurationTime() {
    return this.stageDurationTime;
  }
  
  public short getRank() {
    return this.rank;
  }
  
  public byte getType() {
    return this.type;
  }
}
