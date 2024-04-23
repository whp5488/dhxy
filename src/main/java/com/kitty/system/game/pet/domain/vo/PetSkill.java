package com.kitty.system.game.pet.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class PetSkill implements Serializable {

  @ConstructorProperties({"serial", "type"})
  public PetSkill(int serial, int type) {
    this.serial = serial;
    this.type = type;
  }
  
  private int serial = -1;

  private int type = 0;

  public PetSkill() {}
}
