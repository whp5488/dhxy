package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4137)
public class ReqPetEquip extends Message {
  private String id;
  
  private String eId;

  public void setId(String id) {
    this.id = id;
  }

  public void setEId(String eId) {
    this.eId = eId;
  }

  public String getId() {
    return this.id;
  }
  
  public String getEId() {
    return this.eId;
  }
}
