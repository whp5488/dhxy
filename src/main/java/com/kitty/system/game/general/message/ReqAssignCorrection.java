package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 14338)
public class ReqAssignCorrection extends Message {
  private Integer petId;
  
  private String nutMap;
  
  public void setPetId(Integer petId) {
    this.petId = petId;
  }
  
  public void setNutMap(String nutMap) {
    this.nutMap = nutMap;
  }
  
  public Integer getPetId() {
    return this.petId;
  }
  
  public String getNutMap() {
    return this.nutMap;
  }
}
