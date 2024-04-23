package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 45234)
public class RespReleasePrisonerSuccess extends Message {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public RespReleasePrisonerSuccess() {}
  
  @ConstructorProperties({"id"})
  public RespReleasePrisonerSuccess(int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }
}
