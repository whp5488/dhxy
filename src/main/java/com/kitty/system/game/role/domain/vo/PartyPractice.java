package com.kitty.system.game.role.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class PartyPractice implements Serializable {
  
  private int level = 0;

  private int points = 0;

}
