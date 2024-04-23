package com.kitty.system.game.vo.init;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Item implements Serializable {
  private Integer item;
  
  private Integer num;

  private Integer others = 0;

  public Item(Integer item, Integer num) {
    this.item = item;
    this.num = num;
  }
  
  public Item(Integer item, Integer num, Integer others) {
    this.item = item;
    this.num = num;
    this.others = others;
  }
  
  public Item() {}
}
