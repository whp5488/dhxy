package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;

public class AnimationPlayParam {
  private int fightId;
  
  private int round;
  
  private int predictTime;
  
  private int practicalTime;
  
  public void setFightId(int fightId) {
    this.fightId = fightId;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setPredictTime(int predictTime) {
    this.predictTime = predictTime;
  }
  
  public void setPracticalTime(int practicalTime) {
    this.practicalTime = practicalTime;
  }
  
  public void setEnd(boolean isEnd) {
    this.isEnd = isEnd;
  }
  
  @ConstructorProperties({"fightId", "round", "predictTime", "practicalTime", "isEnd"})
  public AnimationPlayParam(int fightId, int round, int predictTime, int practicalTime, boolean isEnd) {
    this.fightId = fightId;
    this.round = round;
    this.predictTime = predictTime;
    this.practicalTime = practicalTime;
    this.isEnd = isEnd;
  }
  
  public int getFightId() {
    return this.fightId;
  }
  
  public int getRound() {
    return this.round;
  }
  
  public int getPredictTime() {
    return this.predictTime;
  }
  
  public int getPracticalTime() {
    return this.practicalTime;
  }
  
  private boolean isEnd = false;
  
  public boolean isEnd() {
    return this.isEnd;
  }
  
  public AnimationPlayParam() {}
}
