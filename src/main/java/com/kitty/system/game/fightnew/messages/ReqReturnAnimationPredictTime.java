package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 3471)
public class ReqReturnAnimationPredictTime extends Message {
  private int fightId;
  
  private int round;
  
  private int predictTime;
  
  public void setFightId(int fightId) {
    this.fightId = fightId;
  }
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setPredictTime(int predictTime) {
    this.predictTime = predictTime;
  }
  
  @ConstructorProperties({"fightId", "round", "predictTime"})
  public ReqReturnAnimationPredictTime(int fightId, int round, int predictTime) {
    this.fightId = fightId;
    this.round = round;
    this.predictTime = predictTime;
  }
  
  public ReqReturnAnimationPredictTime() {}
  
  public int getFightId() {
    return this.fightId;
  }
  
  public int getRound() {
    return this.round;
  }
  
  public int getPredictTime() {
    return this.predictTime;
  }
}
