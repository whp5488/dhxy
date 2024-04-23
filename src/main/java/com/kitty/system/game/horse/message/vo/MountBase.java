package com.kitty.system.game.horse.message.vo;

import com.kitty.system.common.enter.FiedValue;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

public class MountBase implements Serializable {
  private int grow_up;
  
  private int move_sp;
  
  public void setGrow_up(int grow_up) {
    this.grow_up = grow_up;
  }
  
  public void setMove_sp(int move_sp) {
    this.move_sp = move_sp;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setSkill(List<FiedValue> skill) {
    this.skill = skill;
  }
  
  @ConstructorProperties({"grow_up", "move_sp", "score", "skill"})
  public MountBase(int grow_up, int move_sp, int score, List<FiedValue> skill) {
    this.grow_up = grow_up;
    this.move_sp = move_sp;
    this.score = score;
    this.skill = skill;
  }
  
  public int getGrow_up() {
    return this.grow_up;
  }
  
  public int getMove_sp() {
    return this.move_sp;
  }
  
  private int score = 0;
  
  private List<FiedValue> skill;
  
  public int getScore() {
    return this.score;
  }
  
  public List<FiedValue> getSkill() {
    return this.skill;
  }
  
  public MountBase() {}
}
