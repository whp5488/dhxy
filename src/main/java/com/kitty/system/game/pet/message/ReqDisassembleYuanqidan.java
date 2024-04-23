package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4270)
public class ReqDisassembleYuanqidan extends Message {
  private Integer serial;
  
  private Integer num;
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public Integer getNum() {
    return this.num;
  }
  
  public void setNum(Integer num) {
    this.num = num;
  }
}
