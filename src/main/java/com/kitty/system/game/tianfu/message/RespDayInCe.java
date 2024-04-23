package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 44002)
public class RespDayInCe extends Message {
  private short level;

  @ConstructorProperties({"level"})
  public RespDayInCe(short level) {
    this.level = level;
  }

}
