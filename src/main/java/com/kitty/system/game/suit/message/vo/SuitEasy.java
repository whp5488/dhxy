package com.kitty.system.game.suit.message.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class SuitEasy implements Serializable {
  private String id;
  
  private String name;

  private List<String> equip = new ArrayList<>();
  
  private byte state;

  public SuitEasy(Long id, String name, List<Long> equip, byte state) {
    this.id = String.valueOf(id);
    this.name = name;
    List<String> equipList = new ArrayList<>();
    for (Long key : equip)
      equipList.add(String.valueOf(key)); 
    this.equip = equipList;
    this.state = state;
  }
  
  public SuitEasy() {}
}
