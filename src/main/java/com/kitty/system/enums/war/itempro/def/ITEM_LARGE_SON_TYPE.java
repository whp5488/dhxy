package com.kitty.system.enums.war.itempro.def;

public enum ITEM_LARGE_SON_TYPE {
  ITEM_DEF_TYPE_SKILL_BOOK("技能书", 2);
  
  private int value;
  
  private String note;
  
  ITEM_LARGE_SON_TYPE(String note, int value) {
    this.note = note;
    this.value = value;
  }
  
  public String getNote() {
    return this.note;
  }
  
  public int getValue() {
    return this.value;
  }
}
