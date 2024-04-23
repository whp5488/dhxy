package com.kitty.system.game.task.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class RankHallQuestion implements Serializable {
  private long i;
  
  private String n;
  
  private int t;
  
  @ConstructorProperties({"i", "n", "t"})
  public RankHallQuestion(long i, String n, int t) {
    this.i = i;
    this.n = n;
    this.t = t;
  }
  
  public RankHallQuestion() {}

}
