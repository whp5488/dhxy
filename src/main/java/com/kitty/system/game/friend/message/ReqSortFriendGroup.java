package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45181)
public class ReqSortFriendGroup extends Message {
  private String id;
  
  private short sort;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setSort(short sort) {
    this.sort = sort;
  }
  
  public String getId() {
    return this.id;
  }
  
  public short getSort() {
    return this.sort;
  }
}
