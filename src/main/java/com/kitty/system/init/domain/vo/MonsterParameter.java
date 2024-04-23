package com.kitty.system.init.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class MonsterParameter implements Serializable {
  private Integer serial;
  
  private double percent;
  
  private String type;
  
  private byte round;

}
