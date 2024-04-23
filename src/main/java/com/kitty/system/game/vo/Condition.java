package com.kitty.system.game.vo;

import com.kitty.system.game.vo.init.Item;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class Condition {
  private Map<Integer, Integer> map;
  
  private List<Item> list;
  
  @ConstructorProperties({"map", "list"})
  public Condition(Map<Integer, Integer> map, List<Item> list) {
    this.map = map;
    this.list = list;
  }
  
  public Condition() {}

}
