package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 45060)
public class RespStateRefresh extends Message {
  private List<FiedValue> list;
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  @ConstructorProperties({"list"})
  public RespStateRefresh(List<FiedValue> list) {
    this.list = list;
  }
  
  public RespStateRefresh() {}
  
  public List<FiedValue> getList() {
    return this.list;
  }
}
