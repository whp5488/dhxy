package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.friend.message.vo.Pla;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 45207)
public class RespFriend extends Message {
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setList(List<Pla> list) {
    this.list = list;
  }
  
  @ConstructorProperties({"type", "list"})
  public RespFriend(byte type, List<Pla> list) {
    this.type = type;
    this.list = list;
  }
  
  private byte type = 0;
  
  private List<Pla> list;
  
  public byte getType() {
    return this.type;
  }
  
  public List<Pla> getList() {
    return this.list;
  }
  
  public RespFriend() {}
}
