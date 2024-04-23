package com.kitty.system.game.confirm.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20736)
public class ReqConfirmResult extends Message {
  private String select;
  
  public void setSelect(String select) {
    this.select = select;
  }
  
  public String getSelect() {
    return this.select;
  }
}
