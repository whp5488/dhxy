package com.kitty.system.game.partner.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4205)
public class ReqChangeDefaultSkill extends Message {
  private int pId;
  
  private int skillId;
  
  public void setPId(int pId) {
    this.pId = pId;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public int getPId() {
    return this.pId;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
}
