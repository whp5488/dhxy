package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45233)
public class ReqPrisonerRelease extends Message {
  private int id;
  
  public int getId() {
    return this.id;
  }
}
