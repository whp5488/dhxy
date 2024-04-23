package com.kitty.system.enums.war.wardef;

public enum RACE {
  RACE_REN("人族", 1),
  RACE_XIAN("仙族", 2),
  RACE_MO("魔族", 3),
  RACE_GUI("鬼族", 4),
  RACE_LONG("龙族", 5);
  
  RACE(String note, int value) {
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
