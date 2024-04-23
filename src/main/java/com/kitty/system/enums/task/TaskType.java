package com.kitty.system.enums.task;

public enum TaskType {
  MAIN_LINE(1),
  SCHOOL(2),
  TREASURE_MAP(3),
  PARTY(4),
  TOWER(5),
  LOOK_FOR(6),
  FIVE(7),
  CATCHES_GHOSTS(8),
  GODS_COURT(9),
  QUESTION_DLS(10),
  CARNIVAL(11),
  DRAGON(12),
  CRUSADE_RENEGADE(13),
  XM(14),
  KJ(15),
  LOOP(20),
  MWK_FRONT(2001);
  
  private int id;
  
  TaskType(int id) {
    this.id = id;
  }
  
  public int getId() {
    return this.id;
  }
}
