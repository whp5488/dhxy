package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 45247)
public class RespWaterClashResult extends Message {
  private byte result;
  
  public void setResult(byte result) {
    this.result = result;
  }
  
  public void setScore(short score) {
    this.score = score;
  }
  
  public void setFeats(short feats) {
    this.feats = feats;
  }
  
  public void setItself(List<MemberInfo> itself) {
    this.itself = itself;
  }
  
  public void setEnemy(List<MemberInfo> enemy) {
    this.enemy = enemy;
  }
  
  public byte getResult() {
    return this.result;
  }
  
  private short score = 0;
  
  public short getScore() {
    return this.score;
  }
  
  private short feats = 0;
  
  public short getFeats() {
    return this.feats;
  }
  
  private List<MemberInfo> itself = new ArrayList<>(5);
  
  public List<MemberInfo> getItself() {
    return this.itself;
  }
  
  private List<MemberInfo> enemy = new ArrayList<>(5);
  
  public List<MemberInfo> getEnemy() {
    return this.enemy;
  }
}
