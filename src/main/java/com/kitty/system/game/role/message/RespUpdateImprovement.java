package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@MessageMeta(module = 65511)
public class RespUpdateImprovement extends Message {
  private int id;
  
  private List<FiedValue> list;

  public RespUpdateImprovement() {}
  
  public RespUpdateImprovement(int id, List<FiedValue> list) {
    this.id = id;
    this.list = list;
  }
}
