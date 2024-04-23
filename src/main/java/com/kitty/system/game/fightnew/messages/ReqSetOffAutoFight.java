package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 3473)
public class ReqSetOffAutoFight extends Message {
  private byte autoFight;
  
  public void setAutoFight(byte autoFight) {
    this.autoFight = autoFight;
  }
  
  @ConstructorProperties({"autoFight"})
  public ReqSetOffAutoFight(byte autoFight) {
    this.autoFight = autoFight;
  }
  
  public ReqSetOffAutoFight() {}
  
  public byte getAutoFight() {
    return this.autoFight;
  }
}
