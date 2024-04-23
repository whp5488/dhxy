package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4150)
public class ReqOpenMenu extends Message {
  private int npcId;
  
  private int type;
  
  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public int getType() {
    return this.type;
  }
}
