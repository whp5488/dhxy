package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 45317)
public class RespDelFriendGroup extends Message {
  private String id;
  
  public void setId(String id) {
    this.id = id;
  }
  
  @ConstructorProperties({"id"})
  public RespDelFriendGroup(String id) {
    this.id = id;
  }
  
  public RespDelFriendGroup() {}
  
  public String getId() {
    return this.id;
  }
}
