package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6552)
public class RespRefreshStore extends Message {
  private String saveLife;
  
  private String saveMana;
  
  public void setSaveLife(String saveLife) {
    this.saveLife = saveLife;
  }
  
  public void setSaveMana(String saveMana) {
    this.saveMana = saveMana;
  }
  
  public RespRefreshStore() {}
  
  public String getSaveLife() {
    return this.saveLife;
  }
  
  public String getSaveMana() {
    return this.saveMana;
  }
  
  public RespRefreshStore(Long saveLife, Long saveMana) {
    this.saveLife = String.valueOf(saveLife);
    this.saveMana = String.valueOf(saveMana);
  }
}
