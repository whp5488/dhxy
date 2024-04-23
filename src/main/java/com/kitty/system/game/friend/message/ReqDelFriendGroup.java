package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45180)
public class ReqDelFriendGroup extends Message {
  private String id;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getId() {
    return this.id;
  }
}
