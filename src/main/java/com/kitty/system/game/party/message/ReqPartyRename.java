package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32836)
public class ReqPartyRename extends Message {
  private String name;

  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}
