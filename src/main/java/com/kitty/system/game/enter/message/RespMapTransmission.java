package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.List;

@MessageMeta(module = 65531)
public class RespMapTransmission extends Message {
  public void setAdd_exit(Integer add_exit) {
    this.add_exit = add_exit;
  }
  
  public void setList(List<MapTransmission> list) {
    this.list = list;
  }
  
  private Integer add_exit = Integer.valueOf(1);
  
  private List<MapTransmission> list;
  
  public Integer getAdd_exit() {
    return this.add_exit;
  }
  
  public List<MapTransmission> getList() {
    return this.list;
  }
}
