package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11834)
public class ReqControlPartyChannel extends Message {
  private int id;
  
  private String name;
  
  private byte type;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setType(byte type) {
    this.type = type;
  }

  public int getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public byte getType() {
    return this.type;
  }
}
