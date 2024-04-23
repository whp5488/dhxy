package com.kitty.system.game.enter;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.enter.vo.Title;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 62209)
public class RespTitleList extends Message {
  private List<Title> list;
  
  public void setList(List<Title> list) {
    this.list = list;
  }
  
  public RespTitleList() {}
  
  @ConstructorProperties({"list"})
  public RespTitleList(List<Title> list) {
    this.list = list;
  }
  
  public List<Title> getList() {
    return this.list;
  }
}
