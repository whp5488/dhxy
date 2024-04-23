package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum PET_SKILL_TERM_DICT {
  PRO_BASS_PET_N("否宠物", "pet"),
  PRO_BASS_DOWN_N("否死亡", "down"),
  OTHER("其他", "other");
  
  PET_SKILL_TERM_DICT(String name, String value) {
    this.name = name;
    this.value = value;
  }
  
  private String name;
  
  private String value;
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public static List<String> toList() {
    List<String> list = Lists.newArrayList();
    for (PET_SKILL_TERM_DICT n : values())
      list.add(n.getValue()); 
    return list;
  }
}
