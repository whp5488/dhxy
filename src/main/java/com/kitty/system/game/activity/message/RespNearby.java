package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.message.vo.Nearby;
import java.beans.ConstructorProperties;
import java.util.List;

@MessageMeta(module = 20466)
public class RespNearby extends Message {
  public List<Nearby> list;
  
  public void setList(List<Nearby> list) {
    this.list = list;
  }
  
  public RespNearby() {}
  
  @ConstructorProperties({"list"})
  public RespNearby(List<Nearby> list) {
    this.list = list;
  }
  
  public List<Nearby> getList() {
    return this.list;
  }
}
