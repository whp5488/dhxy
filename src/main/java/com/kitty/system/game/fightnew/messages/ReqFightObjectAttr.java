package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4243)
public class ReqFightObjectAttr extends Message {
  private int fightId;
  
  public void setFightId(int fightId) {
    this.fightId = fightId;
  }
  
  public int getFightId() {
    return this.fightId;
  }
}
