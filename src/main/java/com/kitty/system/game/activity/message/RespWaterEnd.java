package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.message.vo.WaterMatchResultInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 49163)
public class RespWaterEnd extends Message {
  public void setRound(byte round) {
    this.round = round;
  }
  
  public void setRank(short rank) {
    this.rank = rank;
  }
  
  public void setItself(List<Integer> itself) {
    this.itself = itself;
  }
  
  public void setMatchResult(List<WaterMatchResultInfo> matchResult) {
    this.matchResult = matchResult;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setFeats(int feats) {
    this.feats = feats;
  }
  
  private byte round = 0;
  
  public byte getRound() {
    return this.round;
  }
  
  private short rank = 0;
  
  private List<Integer> itself;
  
  public short getRank() {
    return this.rank;
  }
  
  public List<Integer> getItself() {
    return this.itself;
  }
  
  private List<WaterMatchResultInfo> matchResult = new ArrayList<>(3);
  
  public List<WaterMatchResultInfo> getMatchResult() {
    return this.matchResult;
  }
  
  private int score = 0;
  
  public int getScore() {
    return this.score;
  }
  
  private int feats = 0;
  
  public int getFeats() {
    return this.feats;
  }
}
