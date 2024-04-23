package com.kitty.system.enums.pet;

public enum ConciseType {
  COMMON(Integer.valueOf(1), Integer.valueOf(1)),
  HIGHER(Integer.valueOf(2), Integer.valueOf(5)),
  SUPRA(Integer.valueOf(3), Integer.valueOf(17));
  
  ConciseType(Integer note, Integer value) {
    this.note = note;
    this.value = value;
  }
  
  private Integer note;
  
  private Integer value;
  
  public Integer getNote() {
    return this.note;
  }
  
  public Integer getValue() {
    return this.value;
  }
  
  public static Integer getValue(Integer note) {
    Integer value = COMMON.value;
    for (ConciseType concise : values()) {
      if (concise.getNote().equals(note)) {
        value = concise.getValue();
        break;
      } 
    } 
    return value;
  }
}
