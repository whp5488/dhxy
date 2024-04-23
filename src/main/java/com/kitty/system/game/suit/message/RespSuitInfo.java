package com.kitty.system.game.suit.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 4755)
public class RespSuitInfo extends Message {

  @ConstructorProperties({"is", "num"})
  public RespSuitInfo(byte is, int num) {
    this.is = is;
    this.num = num;
  }
  
  private byte is = 0;

  private int num = 0;

  public RespSuitInfo() {}
}
