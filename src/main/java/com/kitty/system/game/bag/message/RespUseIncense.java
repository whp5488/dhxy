package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 42160)
public class RespUseIncense extends Message {
  private Integer serial;
  
  private Integer residueTimes;

  public RespUseIncense() {}
  
  public RespUseIncense(Integer serial, Integer residueTimes) {
    this.serial = serial;
    this.residueTimes = residueTimes;
  }
}
