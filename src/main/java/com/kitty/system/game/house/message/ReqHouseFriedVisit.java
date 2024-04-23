package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20605)
public class ReqHouseFriedVisit extends Message {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }
}
