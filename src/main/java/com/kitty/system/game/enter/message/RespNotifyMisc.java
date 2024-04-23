package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.annotation.StringField;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20480)
public class RespNotifyMisc extends Message {
  @StringField(1)
  private String msg;
  
  private int time;
  
  private int friendId;
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public void setFriendId(int friendId) {
    this.friendId = friendId;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public int getTime() {
    return this.time;
  }
  
  public int getFriendId() {
    return this.friendId;
  }
  
  public RespNotifyMisc(String msg) {
    this.msg = msg;
    this.time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
  }
  
  public RespNotifyMisc() {}
}
