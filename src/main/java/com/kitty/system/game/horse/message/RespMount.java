package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.horse.message.vo.Mount;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 41041)
public class RespMount extends Message {
  private List<Mount> list;
  
  public void setList(List<Mount> list) {
    this.list = list;
  }
  
  @ConstructorProperties({"list"})
  public RespMount(List<Mount> list) {
    this.list = list;
  }
  
  public RespMount() {}
  
  public List<Mount> getList() {
    return this.list;
  }
}
