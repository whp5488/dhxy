package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.List;

@MessageMeta(module = 42140)
public class RespAssistPetList extends Message {
  private Integer serial;
  
  private String name;
  
  private List<Integer> petIds;
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public List<Integer> getPetIds() {
    return this.petIds;
  }
  
  public void setPetIds(List<Integer> petIds) {
    this.petIds = petIds;
  }
}
