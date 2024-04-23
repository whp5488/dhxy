package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 3581)
public class RespEndCombat extends Message {
  public void setFlag(short flag) {
    this.flag = flag;
  }
  
  @ConstructorProperties({"flag"})
  public RespEndCombat(short flag) {
    this.flag = flag;
  }
  
  private short flag = 1;
  
  public short getFlag() {
    return this.flag;
  }
  
  public RespEndCombat() {}
}
