package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4192)
public class ReqLoadExistedChar extends Message {
  private String id;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public String getId() {
    return this.id;
  }
}
