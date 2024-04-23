package com.kitty.system.enums.war.wardef;

public enum LOGIC {
  LOGICTYPE_HERO("英雄", 1),
  LOGICTYPE_PET("宠物", 2),
  LOGICTYPE_PARTNER("伙伴", 3),
  HORSE("坐骑", 4),
  LOGICTYPE_MONSTER("怪物", 5),
  NPC("NPC", 9);
  
  LOGIC(String note, int value) {
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
