package com.kitty.system.game.chat.message.vo;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 24074)
public class RespLookonChannelMessage extends Message {
  private String sender;
  
  private String msg;
  
  public RespLookonChannelMessage(String msg) {
    this.msg = msg;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setSender(String sender) {
    this.sender = sender;
  }
}
