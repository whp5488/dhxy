package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 45199)
public class ReqAlterFriendGroup extends Message {
  private String id;
  
  private String name;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setList(List<Integer> list) {
    this.list = list;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getName() {
    return this.name;
  }
  
  private List<Integer> list = new ArrayList<>();
  
  public List<Integer> getList() {
    return this.list;
  }
}
