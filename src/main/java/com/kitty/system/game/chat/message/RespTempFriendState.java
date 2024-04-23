package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53569)
public class RespTempFriendState extends Message {
  private String gid;
  
  private int online;
  
  public void setGid(String gid) {
    this.gid = gid;
  }
  
  public void setOnline(int online) {
    this.online = online;
  }
  
  public String toString() {
    return "RespTempFriendState(gid=" + getGid() + ", online=" + getOnline() + ")";
  }
  
  public String getGid() {
    return this.gid;
  }
  
  public int getOnline() {
    return this.online;
  }
}
