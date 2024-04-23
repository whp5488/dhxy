package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 65527)
public class RespUpdate extends Message {
  private int id;
  
  private List<FiedValue> list;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  public RespUpdate() {}
  
  @ConstructorProperties({"id", "list"})
  public RespUpdate(int id, List<FiedValue> list) {
    this.id = id;
    this.list = list;
  }
  
  public int getId() {
    return this.id;
  }
  
  public List<FiedValue> getList() {
    return this.list;
  }
}
