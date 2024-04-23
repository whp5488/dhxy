package com.kitty.system.enums.email;

public enum Sender {
  TYPE_1(1, "系统"),
  TYPE_2(2, "帮派"),
  TYPE_90247(90247, "礼部");
  
  Sender(int note, String value) {
    this.note = note;
    this.value = value;
  }
  
  private int note;
  
  private String value;
  
  public int getNote() {
    return this.note;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public static String getName(int note) {
    String name = "系统";
    for (Sender n : values()) {
      if (n.note == note) {
        name = n.getValue();
        break;
      } 
    } 
    return name;
  }
}
