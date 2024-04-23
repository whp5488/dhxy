package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 32779)
public class RespPetEquipDisplace extends Message {
  private String main;
  
  private String second;

  @ConstructorProperties({"main", "second"})
  public RespPetEquipDisplace(String main, String second) {
    this.main = main;
    this.second = second;
  }
  
  public RespPetEquipDisplace() {}

}
