package com.kitty.system.game.plug.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45302)
public class RespPlugExecute extends Message {
  private List<Integer> list;
  
  @ConstructorProperties({"list"})
  public RespPlugExecute(List<Integer> list) {
    this.list = list;
  }
  
  public RespPlugExecute() {}

}
