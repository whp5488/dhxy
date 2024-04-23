package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.friend.message.vo.SearchFriend;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 61555)
public class RespSearchFriend extends Message {
  
  public void setList(List<SearchFriend> list) {
    this.list = list;
  }
  
  private List<SearchFriend> list = new ArrayList<>();
  
  public List<SearchFriend> getList() {
    return this.list;
  }
}
