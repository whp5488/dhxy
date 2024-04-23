package com.kitty.system.game.tianfu.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 4404)
public class ReqTianFuWear extends Message {
  private byte type;
  
  private String id;
  
  private short grid;

  @ConstructorProperties({"type", "id", "grid"})
  public ReqTianFuWear(byte type, String id, short grid) {
    this.type = type;
    this.id = id;
    this.grid = grid;
  }
  
  public ReqTianFuWear() {}

}
