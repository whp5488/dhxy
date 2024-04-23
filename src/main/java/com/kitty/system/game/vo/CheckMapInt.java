package com.kitty.system.game.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Map;

@Setter
@Getter
public class CheckMapInt implements Serializable {
  private boolean flag;
  
  private Map<Integer, Integer> lack;
  
  private Map<Integer, Integer> has;
  
  public CheckMapInt() {}
  
  @ConstructorProperties({"flag", "lack", "has"})
  public CheckMapInt(boolean flag, Map<Integer, Integer> lack, Map<Integer, Integer> has) {
    this.flag = flag;
    this.lack = lack;
    this.has = has;
  }

}
