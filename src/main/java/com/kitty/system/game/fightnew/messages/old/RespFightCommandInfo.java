package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 36889)
public class RespFightCommandInfo extends Message {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setAction(int action) {
    this.action = action;
  }
  
  public void setBorn(int born) {
    this.born = born;
  }
  
  public int getId() {
    return this.id;
  }
  
  private int type = 0;
  
  public int getType() {
    return this.type;
  }
  
  private int skillId = 0;
  
  public int getSkillId() {
    return this.skillId;
  }
  
  private int action = 1;
  
  public int getAction() {
    return this.action;
  }
  
  private int born = 0;
  
  public int getBorn() {
    return this.born;
  }
}
