package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 32776)
public class ReqUpgradeEquip extends Message {
  private int pos;
  
  private int type;
  
  @ConstructorProperties({"pos", "type", "operationType", "param"})
  public ReqUpgradeEquip(int pos, int type, int operationType, String param) {
    this.pos = pos;
    this.type = type;
    this.operationType = operationType;
    this.param = param;
  }

  private int operationType = 0;
  
  private String param;

  public ReqUpgradeEquip() {}
}
