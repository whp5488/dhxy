package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9129)
public class RespGeneralNotify extends Message {
  private int notify;
  
  private String value;
  
  public void setNotify(int notify) {
    this.notify = notify;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  public int getNotify() {
    return this.notify;
  }
  
  public String getValue() {
    return this.value;
  }
}
