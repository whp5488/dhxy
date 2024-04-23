package com.kitty.system.game.pet.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class PetCorrection implements Serializable {
  
  private int level = 0;

  private long exp = 0L;

  private int points = 0;

}
