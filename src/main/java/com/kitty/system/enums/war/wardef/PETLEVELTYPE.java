package com.kitty.system.enums.war.wardef;

public enum PETLEVELTYPE {
  Pet_LevelType_Normal("普通召唤兽", 1),
  Pet_LevelType_Senior("高级召唤兽", 2),
  Pet_LevelType_SS("特殊", 3),
  Pet_LevelType_GJSH("神兽", 4),
  Pet_LevelType_TSSS("珍惜神兽", 5);
  
  PETLEVELTYPE(String note, int value) {
    this.note = note;
    this.value = value;
  }
  
  private String note;
  
  private int value;
  
  public String getNote() {
    return this.note;
  }
  
  public int getValue() {
    return this.value;
  }
}
