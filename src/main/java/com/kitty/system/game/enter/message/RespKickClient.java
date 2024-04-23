package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 53405)
public class RespKickClient extends Message {
  private String tips;
  
  public void setTips(String tips) {
    this.tips = tips;
  }
  
  @ConstructorProperties({"tips"})
  public RespKickClient(String tips) {
    this.tips = tips;
  }
  
  public RespKickClient() {}
  
  public String getTips() {
    return this.tips;
  }
}
