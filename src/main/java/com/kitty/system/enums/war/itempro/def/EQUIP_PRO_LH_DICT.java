package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum EQUIP_PRO_LH_DICT {
  GG("根骨", 80001),
  LX("灵性", 80002),
  LL("力量", 80003),
  MJ("敏捷", 80004),
  SPEED("速度", 80052),
  HIT_RATIO("命中率", 80100),
  MISS_RATIO("闪躲率(物理)", 80101),
  COMBO_NUM("连击次数", 80103),
  COMBO_RATIO("连击率", 80104),
  RAGE_RATIO("狂暴率", 80105),
  CONQUER_EXTENT("破防程度", 80106),
  CONQUER_RATIO("破防概率", 80107),
  K_HL("抗混乱", 80200),
  K_DU("抗毒", 80201),
  K_HS("抗昏睡", 80202),
  K_FY("抗封印-冰封", 80203),
  K_F("抗风", 80204),
  K_L("抗雷", 80205),
  K_S("抗水", 80206),
  K_H("抗火", 80207),
  K_ZS("抗震慑", 80208),
  K_SS("抗三尸-抗吸血", 80209),
  K_GH("抗鬼火-抗哀嚎", 80210),
  K_YW("抗遗忘", 80211),
  WL_XS("物理吸收", 80212),
  OMIT_K_HL("忽视抗混乱", 80300),
  OMIT_K_D("忽视抗毒", 80301),
  OMIT_K_HS("忽视抗昏睡", 80302),
  OMIT_K_FY("忽视抗封印", 80303),
  OMIT_K_F("忽视抗风", 80304),
  OMIT_K_L("忽视抗雷", 80305),
  OMIT_K_S("忽视抗水", 80306),
  OMIT_K_H("忽视抗火", 80307),
  OMIT_K_ZS("忽视抗震摄", 80308),
  OMIT_K_SS("忽视抗三尸-抗吸血", 80309),
  OMIT_K_GH("忽视抗鬼火-抗哀嚎", 80310),
  OMIT_K_YW("忽视抗遗忘", 80311),
  F_KB_RATIO("风系狂暴率", 80400),
  L_KB_RATIO("雷系狂暴率", 80401),
  S_KB_RATIO("水系狂暴率", 80402),
  H_KB_RATIO("火系狂暴率", 80403),
  SS_KB_RATIO("三尸狂暴率", 80404),
  GH_KB_RATIO("鬼火狂暴率", 80405),
  RISE_SPEED("加强加速", 80500),
  RISE_DEF("加强加防", 80501),
  RISE_ATTACK("加强加攻", 80502),
  RISE_PJ("加强破甲", 80503),
  RISE_ZJ("加强震击", 80504),
  RISE_ZY("加强治愈", 80505),
  RISE_QHS("加强横扫", 80506),
  Q_KJ("强力克金", 80605),
  Q_KM("强力克木", 80606),
  Q_KS("强力克水", 80607),
  Q_KH("强力克火", 80608),
  Q_KT("强力克土", 80609);
  
  EQUIP_PRO_LH_DICT(String name, int pro) {
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
    for (EQUIP_PRO_LH_DICT n : values())
      list.add(Integer.valueOf(n.getPro())); 
    return list;
  }
}
