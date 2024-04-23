package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 53360)
public class RespPreviewPet extends Message {
  private Integer petId;
  
  @ConstructorProperties({"petId", "type", "color"})
  public RespPreviewPet(Integer petId, Integer type, String color) {
    this.petId = petId;
    this.type = type;
    this.color = color;
  }

  private Integer type = Integer.valueOf(0);

  private String color = "";

  public RespPreviewPet() {}
}
