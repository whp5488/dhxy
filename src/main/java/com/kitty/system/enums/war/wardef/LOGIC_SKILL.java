package com.kitty.system.enums.war.wardef;

public enum LOGIC_SKILL {
  LOGIC_TYPE_MARRY_SKILL("结婚技能", 1),
  LOGIC_TYPE_SKILL("英雄技能", 3),
  LOGIC_TYPE_PET_SKILL("宠物技能", 8);
  
  LOGIC_SKILL(String note, int value) {
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
