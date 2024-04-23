package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 3470)
public class ReqFightHandle extends Message {
  private int round;
  
  private int autoFight;
  
  private int attackerId;
  
  private int targetId;
  
  private int type;
  
  private int skillId;
  
  public void setRound(int round) {
    this.round = round;
  }
  
  public void setAutoFight(int autoFight) {
    this.autoFight = autoFight;
  }
  
  public void setAttackerId(int attackerId) {
    this.attackerId = attackerId;
  }
  
  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  @ConstructorProperties({"round", "autoFight", "attackerId", "targetId", "type", "skillId"})
  public ReqFightHandle(int round, int autoFight, int attackerId, int targetId, int type, int skillId) {
    this.round = round;
    this.autoFight = autoFight;
    this.attackerId = attackerId;
    this.targetId = targetId;
    this.type = type;
    this.skillId = skillId;
  }
  
  public ReqFightHandle() {}
  
  public int getRound() {
    return this.round;
  }
  
  public int getAutoFight() {
    return this.autoFight;
  }
  
  public int getAttackerId() {
    return this.attackerId;
  }
  
  public int getTargetId() {
    return this.targetId;
  }
  
  public int getType() {
    return this.type;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
}
