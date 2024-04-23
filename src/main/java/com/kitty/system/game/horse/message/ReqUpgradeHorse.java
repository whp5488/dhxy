package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 41040)
public class ReqUpgradeHorse extends Message {
  private short type;
  
  @ConstructorProperties({"type", "operationType", "rests", "param"})
  public ReqUpgradeHorse(short type, byte operationType, int rests, String param) {
    this.type = type;
    this.operationType = operationType;
    this.rests = rests;
    this.param = param;
  }

  private byte operationType = 0;

  private int rests = 0;
  
  private String param;

  public ReqUpgradeHorse() {}
}
