package com.kitty.system.game.pet.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 45338)
public class RespPetCorrection extends Message {
  private Integer petId;

  @ConstructorProperties({"petId", "level", "exp", "points", "nutMap"})
  public RespPetCorrection(Integer petId, int level, String exp, int points, String nutMap) {
    this.petId = petId;
    this.level = level;
    this.exp = exp;
    this.points = points;
    this.nutMap = nutMap;
  }

  private int level = 0;

  private String exp = "0";

  private int points = 0;
  
  private String nutMap;

  public RespPetCorrection() {}
}
