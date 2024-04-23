package com.kitty.system.game.role.domain;

import com.kitty.system.common.enter.FiedValue;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class Rune implements Serializable {
  private Long id;
  
  private Integer roleId;
  
  private String name;
  
  private Integer type;
  
  private List<FiedValue> position;
}
