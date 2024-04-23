package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 65507)
public class RespPetInfo extends Message {
  private Integer roleId;
  
  private Integer petId;
  
  private String petName;
  
  private Integer petType;
  
  private String color;
  
  @ConstructorProperties({"roleId", "petId", "petName", "petType", "color", "combat"})
  public RespPetInfo(Integer roleId, Integer petId, String petName, Integer petType, String color, Integer combat) {
    this.roleId = roleId;
    this.petId = petId;
    this.petName = petName;
    this.petType = petType;
    this.color = color;
    this.combat = combat;
  }

  private Integer combat = Integer.valueOf(0);

  public RespPetInfo() {}
}
