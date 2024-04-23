package com.kitty.system.enums.war.itempro.def;

import java.util.HashMap;
import java.util.Map;

public enum EQUIP_PRO_DICT_SCORE {
  RISE_ATTACK("加强加攻", PRO_DICT_TYPE.RISE_ATTACK.getDist(), 5.0D),
  RISE_DEF("加强加防", PRO_DICT_TYPE.RISE_DEF.getDist(), 5.0D),
  RISE_SPEED("加强加速", PRO_DICT_TYPE.RISE_SPEED.getDist(), 5.0D),
  Atk("攻击", PRO_DICT_TYPE.ATTACK.getDist(), 0.275D),
  ATTACK_PCT("攻击百分比", PRO_DICT_TYPE.ATTACK_PCT.getDist(), 7.5D),
  COMBO_NUM("连击次数", PRO_DICT_TYPE.COMBO_NUM.getDist(), 25.0D),
  SPEED("速度", PRO_DICT_TYPE.SPEED.getDist(), 17.0D),
  MJ("敏捷", PRO_DICT_TYPE.MJ.getDist(), 15.0D),
  GG("根骨", PRO_DICT_TYPE.GG.getDist(), 15.0D),
  LL("力量", PRO_DICT_TYPE.LL.getDist(), 15.0D),
  LX("灵性", PRO_DICT_TYPE.LX.getDist(), 15.0D),
  RISE_PJ("加强破甲", PRO_DICT_TYPE.RISE_PJ.getDist(), 5.0D),
  RISE_ZY("加强治愈", PRO_DICT_TYPE.RISE_ZY.getDist(), 5.0D),
  K_HL("抗混乱", PRO_DICT_TYPE.K_HL.getDist(), 3.0D),
  K_H("抗火", PRO_DICT_TYPE.K_H.getDist(), 3.0D),
  OMIT_K_H("忽视抗火", PRO_DICT_TYPE.OMIT_K_H.getDist(), 3.0D),
  K_YW("抗遗忘", PRO_DICT_TYPE.K_YW.getDist(), 3.0D),
  OMIT_K_YW("忽视抗遗忘", PRO_DICT_TYPE.OMIT_K_YW.getDist(), 5.0D),
  K_GH("抗鬼火", PRO_DICT_TYPE.K_GH.getDist(), 3.0D),
  OMIT_K_HL("忽视抗混", PRO_DICT_TYPE.OMIT_K_HL.getDist(), 5.0D),
  OMIT_K_GH("忽视抗鬼火", PRO_DICT_TYPE.OMIT_K_GH.getDist(), 3.0D),
  COMBO_RATIO("连击率", PRO_DICT_TYPE.COMBO_RATIO.getDist(), 2.5D),
  HIT_RATIO("命中率", PRO_DICT_TYPE.HIT_RATIO.getDist(), 2.5D),
  HP_CAP("气血上限", PRO_DICT_TYPE.HP_CAP.getDist(), 0.075D),
  HP_PCT("气血百分比", PRO_DICT_TYPE.HP_PCT.getDist(), 12.0D),
  MP_CAP("法力上限", PRO_DICT_TYPE.MP_CAP.getDist(), 0.075D),
  MP_PCT("法力百分比", PRO_DICT_TYPE.MP_PCT.getDist(), 12.0D),
  WL_XS("物理吸收", PRO_DICT_TYPE.WL_XS.getDist(), 2.5D),
  CONQUER_EXTENT("破防程度", PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), 2.0D),
  CONQUER_RATIO("破防概率", PRO_DICT_TYPE.CONQUER_RATIO.getDist(), 2.0D),
  K_DU("抗毒", PRO_DICT_TYPE.K_DU.getDist(), 3.0D),
  OMIT_K_D("忽视抗毒", PRO_DICT_TYPE.OMIT_K_D.getDist(), 3.0D),
  K_S("抗水", PRO_DICT_TYPE.K_S.getDist(), 3.0D),
  OMIT_K_S("忽视抗水", PRO_DICT_TYPE.OMIT_K_S.getDist(), 3.0D),
  K_SS("抗三尸", PRO_DICT_TYPE.K_SS.getDist(), 3.0D),
  OMIT_K_SS("忽视抗三尸", PRO_DICT_TYPE.OMIT_K_SS.getDist(), 3.0D),
  K_FY("抗封印", PRO_DICT_TYPE.K_FY.getDist(), 3.0D),
  OMIT_K_FY("忽视抗封", PRO_DICT_TYPE.OMIT_K_FY.getDist(), 5.0D),
  K_ZS("抗震慑", PRO_DICT_TYPE.K_ZS.getDist(), 4.7D),
  OMIT_K_ZS("抗震慑", PRO_DICT_TYPE.OMIT_K_ZS.getDist(), 13.0D),
  K_HS("抗昏睡", PRO_DICT_TYPE.K_HS.getDist(), 3.0D),
  OMIT_K_HS("忽视抗睡", PRO_DICT_TYPE.OMIT_K_HS.getDist(), 5.0D),
  SPEED_PCT("速度百分比", PRO_DICT_TYPE.SPEED_PCT.getDist(), 12.0D),
  RISE_QHS("加强横扫", PRO_DICT_TYPE.RISE_QHS.getDist(), 5.0D),
  RISE_ZJ("加强震击", PRO_DICT_TYPE.RISE_ZJ.getDist(), 5.0D),
  RISE_MH("加强魅惑", PRO_DICT_TYPE.RISE_MH.getDist(), 5.0D),
  K_L("抗雷", PRO_DICT_TYPE.K_L.getDist(), 3.0D),
  OMIT_K_L("忽视抗雷", PRO_DICT_TYPE.OMIT_K_L.getDist(), 3.0D),
  MISS_RATIO("闪躲率", PRO_DICT_TYPE.MISS_RATIO.getDist(), 2.5D),
  K_F("抗风", PRO_DICT_TYPE.K_F.getDist(), 3.0D),
  OMIT_K_F("忽视抗风", PRO_DICT_TYPE.OMIT_K_F.getDist(), 3.0D),
  RAGE_RATIO("狂暴率", PRO_DICT_TYPE.RAGE_RATIO.getDist(), 3.0D),
  H_KB_RATIO("火系狂暴率", PRO_DICT_TYPE.H_KB_RATIO.getDist(), 1.7D),
  GH_KB_RATIO("鬼火狂暴率", PRO_DICT_TYPE.GH_KB_RATIO.getDist(), 1.7D),
  S_KB_RATIO("水系狂暴率", PRO_DICT_TYPE.S_KB_RATIO.getDist(), 1.7D),
  SS_KB_RATIO("三尸狂暴率", PRO_DICT_TYPE.SS_KB_RATIO.getDist(), 1.7D),
  L_KB_RATIO("雷系狂暴率", PRO_DICT_TYPE.L_KB_RATIO.getDist(), 1.7D),
  F_KB_RATIO("风系狂暴率", PRO_DICT_TYPE.F_KB_RATIO.getDist(), 1.7D),
  Q_KJ("强力克金", PRO_DICT_TYPE.Q_KJ.getDist(), 2.0D),
  Q_KM("强力克木", PRO_DICT_TYPE.Q_KM.getDist(), 2.0D),
  Q_KS("强力克水", PRO_DICT_TYPE.Q_KS.getDist(), 2.0D),
  Q_KH("强力克火", PRO_DICT_TYPE.Q_KH.getDist(), 2.0D),
  Q_KT("强力克土", PRO_DICT_TYPE.Q_KT.getDist(), 2.0D),
  MAGIC_MISS_RATIO("法术闪躲", PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist(), 66.288D),
  BURN_HURT("伤害加深", PRO_DICT_TYPE.BURN_HURT.getDist(), 40.509D),
  MAGIC_HIT_RATIO("法术命中", PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), 91.146D),
  SH_DK("伤害抵抗", PRO_DICT_TYPE.SH_DK.getDist(), 29.561D);
  
  EQUIP_PRO_DICT_SCORE(String name, int dist, double score) {
    this.name = name;
    this.dist = dist;
    this.score = score;
  }
  
  private String name;
  
  private int dist;
  
  private double score;
  
  public String getName() {
    return this.name;
  }
  
  public int getDist() {
    return this.dist;
  }
  
  public double getScore() {
    return this.score;
  }
  
  public static Map<Integer, Double> toMap() {
    Map<Integer, Double> map = new HashMap<>();
    for (EQUIP_PRO_DICT_SCORE n : values())
      map.put(Integer.valueOf(n.getDist()), Double.valueOf(n.getScore())); 
    return map;
  }
}
