package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 8296)
public class ReqDelFriend extends Message {
  
  public void setList(List<Integer> list) {
    this.list = list;
  }
  
  private List<Integer> list = new ArrayList<>();
  
  public List<Integer> getList() {
    return this.list;
  }
}
