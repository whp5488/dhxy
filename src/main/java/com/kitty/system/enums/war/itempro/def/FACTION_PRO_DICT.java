package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum FACTION_PRO_DICT {
  K_HL("抗混乱", 20200, 0.05D),
  K_DU("抗毒", 20201, 0.05D),
  K_HS("抗昏睡", 20202, 0.05D),
  K_FY("抗封印", 20203, 0.05D),
  K_F("抗风", 20204, 0.05D),
  K_L("抗雷", 20205, 0.05D),
  K_S("抗水", 20206, 0.05D),
  K_H("抗火", 20207, 0.05D),
  K_SS("抗三尸", 20209, 0.05D),
  K_GH("抗鬼火", 20210, 0.05D),
  K_YW("抗遗忘", 20211, 0.05D),
  WL_XS("物理吸收", 20212, 0.04D),
  K_KZ_CAP("抗控制上限", 20214, 0.025D),
  K_TM("抗天魔", 20216, 0.05D),
  K_FG("抗分光", 20217, 0.05D),
  K_QM("抗青面", 20218, 0.05D),
  K_XL("抗小楼", 20219, 0.05D);
  
  FACTION_PRO_DICT(String name, int pro, double gain) {
    this.name = name;
    this.pro = pro;
    this.gain = gain;
  }
  
  private String name;
  
  private int pro;
  
  private double gain;
  
  public String getName() {
    return this.name;
  }
  
  public int getPro() {
    return this.pro;
  }
  
  public double getGain() {
    return this.gain;
  }
  
  public static List<Integer> toList() {
    List<Integer> list = Lists.newArrayList();
    for (FACTION_PRO_DICT n : values())
      list.add(Integer.valueOf(n.getPro())); 
    return list;
  }
  
  public static Map<Integer, Double> toMap() {
    Map<Integer, Double> map = new HashMap<>();
    for (FACTION_PRO_DICT n : values())
      map.put(Integer.valueOf(n.getPro()), Double.valueOf(n.getGain())); 
    return map;
  }
}
