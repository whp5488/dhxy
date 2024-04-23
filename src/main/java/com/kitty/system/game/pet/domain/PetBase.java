package com.kitty.system.game.pet.domain;

import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

@Setter
@Getter
public class PetBase implements Serializable {
  
  @ConstructorProperties({"hp_base", "mp_base", "ap_base", "sp_base", "grow_up", "grade_base", "score"})
  public PetBase(Integer hp_base, Integer mp_base, Integer ap_base, Integer sp_base, Integer grow_up, Integer grade_base, Integer score) {
    this.hp_base = hp_base;
    this.mp_base = mp_base;
    this.ap_base = ap_base;
    this.sp_base = sp_base;
    this.grow_up = grow_up;
    this.grade_base = grade_base;
    this.score = score;
  }
  
  private Integer hp_base = 0;

  private Integer mp_base = 0;

  private Integer ap_base = 0;

  private Integer sp_base = 0;

  private Integer grow_up = 0;

  private Integer grade_base = 0;

  private Integer score = 0;

  public PetBase() {}
}
