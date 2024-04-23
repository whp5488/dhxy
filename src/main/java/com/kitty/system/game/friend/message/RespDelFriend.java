package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 61542)
public class RespDelFriend extends Message {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  @ConstructorProperties({"id"})
  public RespDelFriend(int id) {
    this.id = id;
  }
  
  public RespDelFriend() {}
  
  public int getId() {
    return this.id;
  }
}
