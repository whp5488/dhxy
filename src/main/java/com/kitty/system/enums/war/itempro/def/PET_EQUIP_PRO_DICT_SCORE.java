
package com.kitty.system.enums.war.itempro.def;

import java.util.HashMap;
import java.util.Map;

public enum PET_EQUIP_PRO_DICT_SCORE
{
    Atk("攻击", PRO_DICT_TYPE.ATTACK.getDist(), 0.63), 
    ATTACK_PCT("攻击百分比", PRO_DICT_TYPE.ATTACK_PCT.getDist(), 2.875), 
    K_HL("抗混乱", PRO_DICT_TYPE.K_HL.getDist(), 1.4), 
    RAGE_RATIO("狂暴率", PRO_DICT_TYPE.RAGE_RATIO.getDist(), 3.667), 
    FG_KB_RATIO("分光狂暴率", PRO_DICT_TYPE.FG_KB_RATIO.getDist(), 2.2), 
    K_H("抗火", PRO_DICT_TYPE.K_H.getDist(), 1.533), 
    RISE_H("加强火", PRO_DICT_TYPE.RISE_H.getDist(), 3.0), 
    H_KB_RATIO("火系狂暴率", PRO_DICT_TYPE.H_KB_RATIO.getDist(), 2.3), 
    K_YW("抗遗忘", PRO_DICT_TYPE.K_YW.getDist(), 1.4), 
    K_GH("抗鬼火", PRO_DICT_TYPE.K_GH.getDist(), 1.533), 
    RISE_GH("加强鬼火", PRO_DICT_TYPE.RISE_GH.getDist(), 3.0), 
    GH_KB_RATIO("鬼火狂暴率", PRO_DICT_TYPE.GH_KB_RATIO.getDist(), 2.3), 
    COMBO_RATIO("连击率", PRO_DICT_TYPE.COMBO_RATIO.getDist(), 3.667), 
    HIT_RATIO("命中率", PRO_DICT_TYPE.HIT_RATIO.getDist(), 1.6), 
    HP_CAP("气血上限", PRO_DICT_TYPE.HP_CAP.getDist(), 0.01), 
    HP_PCT("气血百分比", PRO_DICT_TYPE.HP_PCT.getDist(), 2.0), 
    MP_CAP("法力上限", PRO_DICT_TYPE.MP_CAP.getDist(), 0.01), 
    MP_PCT("法力百分比", PRO_DICT_TYPE.MP_PCT.getDist(), 3.833), 
    WL_XS("物理吸收", PRO_DICT_TYPE.WL_XS.getDist(), 1.75), 
    CONQUER_EXTENT("破防程度", PRO_DICT_TYPE.CONQUER_EXTENT.getDist(), 2.4), 
    CONQUER_RATIO("破防概率", PRO_DICT_TYPE.CONQUER_RATIO.getDist(), 2.4), 
    K_DU("抗毒", PRO_DICT_TYPE.K_DU.getDist(), 1.333), 
    QM_KB_RATIO("青面狂暴率", PRO_DICT_TYPE.QM_KB_RATIO.getDist(), 2.2), 
    K_S("抗水", PRO_DICT_TYPE.K_S.getDist(), 1.533), 
    RISE_S("加强水", PRO_DICT_TYPE.RISE_S.getDist(), 3.0), 
    S_KB_RATIO("水系狂暴率", PRO_DICT_TYPE.S_KB_RATIO.getDist(), 2.3), 
    SHOCK_EXTENT("反震程度", PRO_DICT_TYPE.SHOCK_EXTENT.getDist(), 1.333), 
    SHOCK_RATIO("反震率", PRO_DICT_TYPE.SHOCK_RATIO.getDist(), 1.333), 
    K_SS("抗三尸", PRO_DICT_TYPE.K_SS.getDist(), 1.333), 
    K_FY("抗封印", PRO_DICT_TYPE.K_FY.getDist(), 1.4), 
    K_ZS("抗震慑", PRO_DICT_TYPE.K_ZS.getDist(), 3.333), 
    K_HS("抗昏睡", PRO_DICT_TYPE.K_HS.getDist(), 1.4), 
    SPEED("速度", PRO_DICT_TYPE.SPEED.getDist(), 12.5), 
    K_L("抗雷", PRO_DICT_TYPE.K_L.getDist(), 1.533), 
    RISE_L("加强雷", PRO_DICT_TYPE.RISE_L.getDist(), 3.0), 
    L_KB_RATIO("雷系狂暴率", PRO_DICT_TYPE.L_KB_RATIO.getDist(), 2.3), 
    TM_KB_RATIO("天魔暴率", PRO_DICT_TYPE.TM_KB_RATIO.getDist(), 2.2), 
    MISS_RATIO("闪躲率", PRO_DICT_TYPE.MISS_RATIO.getDist(), 3.333), 
    K_F("抗风", PRO_DICT_TYPE.K_F.getDist(), 1.533), 
    RISE_F("加强风", PRO_DICT_TYPE.RISE_F.getDist(), 3.0), 
    F_KB_RATIO("风系狂暴率", PRO_DICT_TYPE.F_KB_RATIO.getDist(), 2.3), 
    XL_KB_RATIO("小楼狂暴率", PRO_DICT_TYPE.XL_KB_RATIO.getDist(), 2.2);
    
    private String name;
    private int dist;
    private double score;
    
    public static Map<Integer, Double> toMap() {
        final Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (final PET_EQUIP_PRO_DICT_SCORE n : values()) {
            map.put(n.getDist(), n.getScore());
        }
        return map;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getDist() {
        return this.dist;
    }
    
    public double getScore() {
        return this.score;
    }
    
    private PET_EQUIP_PRO_DICT_SCORE(final String name, final int dist, final double score) {
        this.name = name;
        this.dist = dist;
        this.score = score;
    }
}
