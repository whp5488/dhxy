package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4210)
public class ReqSearchFriend extends Message {
  private String name;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getName() {
    return this.name;
  }
}
