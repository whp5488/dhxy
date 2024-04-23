package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 45178)
public class ReqAddFriendGroup extends Message {
  private String groupName;
  
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }
  
  public void setList(List<Integer> list) {
    this.list = list;
  }

  public String getGroupName() {
    return this.groupName;
  }
  
  private List<Integer> list = new ArrayList<>();
  
  public List<Integer> getList() {
    return this.list;
  }
}
