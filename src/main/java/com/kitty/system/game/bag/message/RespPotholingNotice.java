package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 4308)
public class RespPotholingNotice extends Message {
  private Integer serial;
  
  private Integer num;

  @ConstructorProperties({"serial", "num"})
  public RespPotholingNotice(Integer serial, Integer num) {
    this.serial = serial;
    this.num = num;
  }

}
