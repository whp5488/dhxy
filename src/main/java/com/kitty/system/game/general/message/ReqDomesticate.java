package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41483)
public class ReqDomesticate extends Message {
  private String id;
  
  private byte type;
  
  private int num;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setNum(int num) {
    this.num = num;
  }
  
  public String getId() {
    return this.id;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public int getNum() {
    return this.num;
  }
}
