package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum ZSXZ_PRO_DICT {
  HP_PCT("气血百分比", 30054),
  MP_PCT("法力百分比", 30055),
  SPEED_PCT("速度百分比", 30057),
  MISS_RATIO("闪躲率(物理)", 30101),
  MAGIC_MISS_RATIO("闪躲率(法术)", 30102),
  LX("抗混乱", 30200),
  K_HL("抗毒", 30201),
  K_HS("抗昏睡", 30202),
  K_FY("抗封印", 30203),
  K_F("抗风", 30204),
  K_L("抗雷", 30205),
  K_S("抗水", 30206),
  K_H("抗火", 30207),
  K_ZS("抗震慑", 30208),
  K_SS("抗三尸", 30209),
  K_GH("抗鬼火", 30210),
  K_YW("抗遗忘", 30211),
  WL_XS("物理吸收-抗物理", 30212),
  SH_DK("伤害抵抗", 30213);
  
  ZSXZ_PRO_DICT(String name, int pro) {
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
    for (ZSXZ_PRO_DICT n : values())
      list.add(Integer.valueOf(n.getPro())); 
    return list;
  }
}
