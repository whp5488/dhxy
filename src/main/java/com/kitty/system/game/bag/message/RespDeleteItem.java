package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 65461)
public class RespDeleteItem extends Message {
  private byte type;
  
  private int pos;

  @ConstructorProperties({"type", "pos"})
  public RespDeleteItem(byte type, int pos) {
    this.type = type;
    this.pos = pos;
  }

}
