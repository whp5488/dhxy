package com.kitty.system.game.plug.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 45303)
public class RespPlugEndTime extends Message {
  
  @ConstructorProperties({"plugEndTime"})
  public RespPlugEndTime(int plugEndTime) {
    this.plugEndTime = plugEndTime;
  }
  
  private int plugEndTime = 0;

  public RespPlugEndTime() {}
}
