package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.pet.domain.PetBase;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 45315)
public class RespUpgradeTaskPet extends Message {
  private Integer petId;
  
  private List<PetBase> list;

  @ConstructorProperties({"petId", "list"})
  public RespUpgradeTaskPet(Integer petId, List<PetBase> list) {
    this.petId = petId;
    this.list = list;
  }
  
  public RespUpgradeTaskPet() {}

}
