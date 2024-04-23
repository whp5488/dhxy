package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum PRACTICE_PET_PRO_DICT {
  HIT_RATIO("命中率", 50100),
  MISS_RATIO("闪躲率(物理)", 50101),
  COMBO_NUM("连击次数", 50103),
  COMBO_RATIO("连击率", 50104),
  RAGE_RATIO("狂暴率", 50105),
  CONQUER_EXTENT("破防程度", 50106),
  CONQUER_RATIO("破防概率", 50107),
  SHOCK_RATIO("反震率", 50108),
  SHOCK_EXTENT("反震程度", 50109),
  K_HL("抗混乱", 50200),
  K_DU("抗毒", 50201),
  K_HS("抗昏睡", 50202),
  K_FY("抗封印", 50203),
  K_F("抗风", 50204),
  K_L("抗雷", 50205),
  K_S("抗水", 50206),
  K_H("抗火", 50207),
  K_SS("抗三尸", 50209),
  K_GH("抗鬼火", 50210),
  K_YW("抗遗忘", 50211),
  WL_XS("物理吸收", 50212);
  
  PRACTICE_PET_PRO_DICT(String name, int pro) {
    this.name = name;
    this.pro = pro;
  }
  
  private String name;
  
  private int pro;
  
  public String getName() {
    return this.name;
  }
  
  public int getPro() {
    return this.pro;
  }
  
  public static List<Integer> toList() {
    List<Integer> list = Lists.newArrayList();
    for (PRACTICE_PET_PRO_DICT n : values())
      list.add(Integer.valueOf(n.getPro())); 
    return list;
  }
}
