package com.kitty.system.game.fightnew.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class RefreshPet implements Serializable {
  private int petId;
  private int type = 0;
}
