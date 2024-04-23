package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 49162)
public class RespWaterStage extends Message {
  public void setRound(byte round) {
    this.round = round;
  }
  
  public void setCurrentSession(byte currentSession) {
    this.currentSession = currentSession;
  }
  
  public void setItself(List<MemberInfo> itself) {
    this.itself = itself;
  }
  
  public void setEnemy(List<MemberInfo> enemy) {
    this.enemy = enemy;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  private byte round = 2;
  
  public byte getRound() {
    return this.round;
  }
  
  private byte currentSession = 1;
  
  public byte getCurrentSession() {
    return this.currentSession;
  }
  
  private List<MemberInfo> itself = new ArrayList<>(5);
  
  public List<MemberInfo> getItself() {
    return this.itself;
  }
  
  private List<MemberInfo> enemy = new ArrayList<>(5);
  
  private byte state;
  
  public List<MemberInfo> getEnemy() {
    return this.enemy;
  }
  
  public byte getState() {
    return this.state;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
}
