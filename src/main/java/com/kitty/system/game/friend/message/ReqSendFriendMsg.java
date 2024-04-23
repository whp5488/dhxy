package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20590)
public class ReqSendFriendMsg extends Message {
  private int att;
  
  private String msg;
  
  public void setAtt(int att) {
    this.att = att;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public int getAtt() {
    return this.att;
  }
  
  public String getMsg() {
    return this.msg;
  }
}
