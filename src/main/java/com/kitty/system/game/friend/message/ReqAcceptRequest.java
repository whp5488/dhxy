package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.List;

@MessageMeta(module = 36868)
public class ReqAcceptRequest extends Message {
  private List<String> list;
  
  private byte accept;
  
  public void setList(List<String> list) {
    this.list = list;
  }
  
  public void setAccept(byte accept) {
    this.accept = accept;
  }

  public List<String> getList() {
    return this.list;
  }
  
  public byte getAccept() {
    return this.accept;
  }
}
