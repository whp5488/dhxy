package com.kitty.system.game.fightnew.bean;

public class RoundParam {
  private int round;
  
  private long beganTime;
  
  private long updateAnimationPredictTime;
  
  private int predictTime;
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setBeganTime(long beganTime) {
    this.beganTime = beganTime;
  }
  
  public void setUpdateAnimationPredictTime(long updateAnimationPredictTime) {
    this.updateAnimationPredictTime = updateAnimationPredictTime;
  }
  
  public void setPredictTime(int predictTime) {
    this.predictTime = predictTime;
  }
  
  public int getRound() {
    return this.round;
  }
  
  public long getBeganTime() {
    return this.beganTime;
  }
  
  public long getUpdateAnimationPredictTime() {
    return this.updateAnimationPredictTime;
  }
  
  public int getPredictTime() {
    return this.predictTime;
  }
  
  public RoundParam(int round) {
    this.round = round;
  }
}
