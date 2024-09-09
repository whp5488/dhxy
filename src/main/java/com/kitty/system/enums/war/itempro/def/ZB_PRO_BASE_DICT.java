package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum ZB_PRO_BASE_DICT
{
    GG("根骨", PRO_DICT_TYPE.GG.getDist()), 
    LX("灵性", PRO_DICT_TYPE.LX.getDist()), 
    LL("力量", PRO_DICT_TYPE.LL.getDist()), 
    MJ("敏捷", PRO_DICT_TYPE.MJ.getDist()), 
    HP_CAP("气血上限", PRO_DICT_TYPE.HP_CAP.getDist()), 
    MP_CAP("法力上限", PRO_DICT_TYPE.MP_CAP.getDist()), 
    SPEED("速度", PRO_DICT_TYPE.SPEED.getDist()), 
    ATTACK("攻击", PRO_DICT_TYPE.ATTACK.getDist()), 
    HP_PCT("气血百分比", PRO_DICT_TYPE.HP_PCT.getDist()), 
    MP_PCT("法力百分比", PRO_DICT_TYPE.MP_PCT.getDist()), 
    ATTACK_PCT("攻击百分比", PRO_DICT_TYPE.ATTACK_PCT.getDist()), 
    SPEED_PCT("速度百分比", PRO_DICT_TYPE.SPEED_PCT.getDist()), 
    HIT_RATIO("命中率", PRO_DICT_TYPE.HIT_RATIO.getDist()), 
    MISS_RATIO("闪躲率(物理)", PRO_DICT_TYPE.MISS_RATIO.getDist()), 
    COMBO_RATIO("连击率", PRO_DICT_TYPE.COMBO_RATIO.getDist()), 
    RAGE_RATIO("狂暴率", PRO_DICT_TYPE.RAGE_RATIO.getDist()), 
    CONQUER_EXTENT("破防程度", PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), 
    CONQUER_RATIO("破防概率", PRO_DICT_TYPE.CONQUER_RATIO.getDist()), 
    K_HL("抗混乱", PRO_DICT_TYPE.K_HL.getDist()), 
    K_DU("抗毒", PRO_DICT_TYPE.K_DU.getDist()), 
    K_HS("抗昏睡", PRO_DICT_TYPE.K_HS.getDist()), 
    K_FY("抗封印-冰封", PRO_DICT_TYPE.K_FY.getDist()), 
    K_F("抗风", PRO_DICT_TYPE.K_F.getDist()), 
    K_L("抗雷", PRO_DICT_TYPE.K_L.getDist()), 
    K_S("抗水", PRO_DICT_TYPE.K_S.getDist()), 
    K_H("抗火", PRO_DICT_TYPE.K_H.getDist()), 
    K_ZS("抗震慑", PRO_DICT_TYPE.K_ZS.getDist()), 
    K_SS("抗三尸-抗吸血", PRO_DICT_TYPE.K_SS.getDist()), 
    K_GH("抗鬼火-抗哀嚎", PRO_DICT_TYPE.K_GH.getDist()), 
    K_YW("抗遗忘", PRO_DICT_TYPE.K_YW.getDist()), 
    WL_XS("物理吸收", PRO_DICT_TYPE.WL_XS.getDist()), 
    OMIT_K_HL("忽视抗混乱", PRO_DICT_TYPE.OMIT_K_HL.getDist()), 
    OMIT_K_D("忽视抗毒", PRO_DICT_TYPE.OMIT_K_D.getDist()), 
    OMIT_K_HS("忽视抗昏睡", PRO_DICT_TYPE.OMIT_K_HS.getDist()), 
    OMIT_K_FY("忽视抗封印", PRO_DICT_TYPE.OMIT_K_FY.getDist()), 
    OMIT_K_F("忽视抗风", PRO_DICT_TYPE.OMIT_K_F.getDist()), 
    OMIT_K_L("忽视抗雷", PRO_DICT_TYPE.OMIT_K_L.getDist()), 
    OMIT_K_S("忽视抗水", PRO_DICT_TYPE.OMIT_K_S.getDist()), 
    OMIT_K_H("忽视抗火", PRO_DICT_TYPE.OMIT_K_H.getDist()), 
    OMIT_K_ZS("忽视抗震摄", PRO_DICT_TYPE.OMIT_K_ZS.getDist()), 
    OMIT_K_SS("忽视抗三尸-抗吸血", PRO_DICT_TYPE.OMIT_K_SS.getDist()), 
    OMIT_K_GH("忽视抗鬼火-抗哀嚎", PRO_DICT_TYPE.OMIT_K_GH.getDist()), 
    OMIT_K_YW("忽视抗遗忘", PRO_DICT_TYPE.OMIT_K_YW.getDist()), 
    RISE_SPEED("加强加速", PRO_DICT_TYPE.RISE_SPEED.getDist()), 
    RISE_DEF("加强加防", PRO_DICT_TYPE.RISE_DEF.getDist()), 
    RISE_ATTACK("加强加攻", PRO_DICT_TYPE.RISE_ATTACK.getDist()), 
    RISE_PJ("加强破甲", PRO_DICT_TYPE.RISE_PJ.getDist()), 
    RISE_ZJ("加强震击", PRO_DICT_TYPE.RISE_ZJ.getDist()), 
    RISE_ZY("加强治愈", PRO_DICT_TYPE.RISE_ZY.getDist()), 
    RISE_QHS("加强横扫", PRO_DICT_TYPE.RISE_QHS.getDist()), 
    MAGIC_MISS_RATIO("法术闪躲", PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist()), 
    BURN_HURT("伤害加深", PRO_DICT_TYPE.BURN_HURT.getDist()), 
    MAGIC_HIT_RATIO("法术命中", PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), 
    SH_DK("伤害抵抗", PRO_DICT_TYPE.SH_DK.getDist());
    
    private String name;
    private int pro;
    
    public static List<Integer> toList() {
        List<Integer> list = Lists.newArrayList();
        for (ZB_PRO_BASE_DICT n : values()) {
            list.add(Integer.valueOf(n.getPro()));
        }
        return list;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getPro() {
        return this.pro;
    }
    
    private ZB_PRO_BASE_DICT(final String name, final int pro) {
        this.name = name;
        this.pro = pro;
    }
}
