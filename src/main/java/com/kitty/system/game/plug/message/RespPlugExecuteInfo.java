package com.kitty.system.game.plug.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 45304)
public class RespPlugExecuteInfo extends Message {
  private int serial;
  
  private int num;
  
  @ConstructorProperties({"serial", "num"})
  public RespPlugExecuteInfo(int serial, int num) {
    this.serial = serial;
    this.num = num;
  }
  
  public RespPlugExecuteInfo() {}

}
