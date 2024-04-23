package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 53718)
public class RespQuestion extends Message {
  private int type;
  
  private int serial;
  
  private int num;
  
  public RespQuestion() {}
  
  @ConstructorProperties({"type", "serial", "num"})
  public RespQuestion(int type, int serial, int num) {
    this.type = type;
    this.serial = serial;
    this.num = num;
  }

}
