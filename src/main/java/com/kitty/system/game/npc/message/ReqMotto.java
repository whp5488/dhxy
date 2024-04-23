package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6890)
public class ReqMotto extends Message {
  private String motto;
  
  public void setMotto(String motto) {
    this.motto = motto;
  }
  
  public String getMotto() {
    return this.motto;
  }
}
