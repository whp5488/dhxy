package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8854)
public class RespRectifyTime extends Message {
  private String reqTime;
  
  public void setReqTime(String reqTime) {
    this.reqTime = reqTime;
  }
  
  public void setRespTime(String respTime) {
    this.respTime = respTime;
  }
  
  public String getReqTime() {
    return this.reqTime;
  }
  
  private String respTime = String.valueOf(System.currentTimeMillis());
  
  public String getRespTime() {
    return this.respTime;
  }
  
  public RespRectifyTime(String reqTime) {
    this.reqTime = reqTime;
  }
  
  public RespRectifyTime() {}
}
