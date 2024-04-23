package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4250)
public class ReqAssistPetList extends Message {
  private Integer serial;
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
}
