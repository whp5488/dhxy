package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.equip.domain.PetEquipEasy;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 65523)
public class RespFlyPetEqInfo extends Message {
  private PetEquipEasy equip;
  
  @ConstructorProperties({"equip"})
  public RespFlyPetEqInfo(PetEquipEasy equip) {
    this.equip = equip;
  }
  
  public RespFlyPetEqInfo() {}

}
