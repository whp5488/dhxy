package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.friend.message.vo.FriendGroup;
import java.util.List;

@MessageMeta(module = 45193)
public class RespFriendGroup extends Message {
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setList(List<FriendGroup> list) {
    this.list = list;
  }
  
  private byte type = 0;
  
  private List<FriendGroup> list;
  
  public byte getType() {
    return this.type;
  }
  
  public List<FriendGroup> getList() {
    return this.list;
  }
}
