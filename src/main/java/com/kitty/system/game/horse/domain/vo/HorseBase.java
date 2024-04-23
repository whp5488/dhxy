package com.kitty.system.game.horse.domain.vo;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.IdentityHashMap;

@Setter
@Getter
public class HorseBase implements Serializable {
  private int grow_up;
  
  private int move_sp;
  
  @ConstructorProperties({"grow_up", "move_sp", "score", "skill"})
  public HorseBase(int grow_up, int move_sp, int score, IdentityHashMap<Integer, Long> skill) {
    this.grow_up = grow_up;
    this.move_sp = move_sp;
    this.score = score;
    this.skill = skill;
  }

  private int score = 0;

  private IdentityHashMap<Integer, Long> skill = new IdentityHashMap<>(3);

  public HorseBase() {}
}
