package com.kitty.system.enums.common;

public enum ADORN {
  WEAPON("武器", 1),
  NECKLACE("项链", 2),
  CLOTH("衣服", 3),
  HAT("头盔", 4),
  SHOES("鞋子", 5),
  CLOAK("披风", 6),
  PENDANT("挂件", 7),
  BELT("腰带", 8),
  RING("戒指", 9),
  BODY("随身特效", 1),
  FOOT("足迹特效", 2),
  WING("翅膀特效", 3);
  
  ADORN(String note, int value) {
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
