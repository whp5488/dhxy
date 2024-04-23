package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;

@Setter
@Getter
@MessageMeta(module = 12025)
public class RespPetEffctInfo extends Message {
  private Integer roleId;
  
  private Integer petId;
  
  private List<PetSkill> list;
  
  @ConstructorProperties({"roleId", "petId", "list"})
  public RespPetEffctInfo(Integer roleId, Integer petId, List<PetSkill> list) {
    this.roleId = roleId;
    this.petId = petId;
    this.list = list;
  }
  
  public RespPetEffctInfo() {}

}
