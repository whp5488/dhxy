package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4155)
public class RespNpcLeave extends Message {
  private int npcId;

  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
}
