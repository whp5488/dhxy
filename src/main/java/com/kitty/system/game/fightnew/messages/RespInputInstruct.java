package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 53716)
public class RespInputInstruct extends Message {
  private int attaId;
  
  private int targetId;
  
  private int action;
  
  private int skillId;
  
  private int sandClock;
  
  public void setAttaId(int attaId) {
    this.attaId = attaId;
  }
  
  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
  
  public void setAction(int action) {
    this.action = action;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setSandClock(int sandClock) {
    this.sandClock = sandClock;
  }
  
  @ConstructorProperties({"attaId", "targetId", "action", "skillId", "sandClock"})
  public RespInputInstruct(int attaId, int targetId, int action, int skillId, int sandClock) {
    this.attaId = attaId;
    this.targetId = targetId;
    this.action = action;
    this.skillId = skillId;
    this.sandClock = sandClock;
  }
  
  public RespInputInstruct() {}
  
  public int getAttaId() {
    return this.attaId;
  }
  
  public int getTargetId() {
    return this.targetId;
  }
  
  public int getAction() {
    return this.action;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  public int getSandClock() {
    return this.sandClock;
  }
}
