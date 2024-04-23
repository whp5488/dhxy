package com.kitty.system.game.ranking.domain;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class RankEquip implements Serializable {
  private long i;
  
  private int s;
  
  private String n;
  
  @ConstructorProperties({"i", "s", "n"})
  public RankEquip(long i, int s, String n) {
    this.i = i;
    this.s = s;
    this.n = n;
  }
  
  public RankEquip() {}

}
