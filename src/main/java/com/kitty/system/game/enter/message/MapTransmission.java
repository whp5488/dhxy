package com.kitty.system.game.enter.message;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MapTransmission {
  private String name;
  
  private Integer X;
  
  private Integer Y;

  private Integer dir = 5;

}
