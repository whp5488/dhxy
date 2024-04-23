package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 24113)
public class RespLoginPreview extends Message {
  private String sid;
  
  public void setSid(String sid) {
    this.sid = sid;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setCookie(String cookie) {
    this.cookie = cookie;
  }
  
  public String getSid() {
    return this.sid;
  }
  
  private int time = 0;
  
  private String cookie;
  
  public int getTime() {
    return this.time;
  }
  
  public String getCookie() {
    return this.cookie;
  }
}
