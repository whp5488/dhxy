package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum EQUIP_PRO_DICT
{
    GG("根骨", 70001), 
    LX("灵性", 70002), 
    LL("力量", 70003), 
    MJ("敏捷", 70004), 
    HP_CAP("气血上限", 70050), 
    MP_CAP("法力上限", 70051), 
    SPEED("速度", 70052), 
    ATTACK("攻击", 70053), 
    HP_PCT("气血百分比", 70054), 
    MP_PCT("法力百分比", 70055), 
    ATTACK_PCT("攻击百分比", 70056), 
    SPEED_PCT("速度百分比", 70057), 
    HIT_RATIO("命中率", 70100), 
    MISS_RATIO("闪躲率(物理)", 70101), 
    COMBO_RATIO("连击率", 70104), 
    RAGE_RATIO("狂暴率", 70105), 
    CONQUER_EXTENT("破防程度", 70106), 
    CONQUER_RATIO("破防概率", 70107), 
    K_HL("抗混乱", 70200), 
    K_DU("抗毒", 70201), 
    K_HS("抗昏睡", 70202), 
    K_FY("抗封印-冰封", 70203), 
    K_F("抗风", 70204), 
    K_L("抗雷", 70205), 
    K_S("抗水", 70206), 
    K_H("抗火", 70207), 
    K_ZS("抗震慑", 70208), 
    K_SS("抗三尸-抗吸血", 70209), 
    K_GH("抗鬼火-抗哀嚎", 70210), 
    K_YW("抗遗忘", 70211), 
    WL_XS("物理吸收", 70212), 
    OMIT_K_HL("忽视抗混乱", 70300), 
    OMIT_K_D("忽视抗毒", 70301), 
    OMIT_K_HS("忽视抗昏睡", 70302), 
    OMIT_K_FY("忽视抗封印", 70303), 
    OMIT_K_F("忽视抗风", 70304), 
    OMIT_K_L("忽视抗雷", 70305), 
    OMIT_K_S("忽视抗水", 70306), 
    OMIT_K_H("忽视抗火", 70307), 
    OMIT_K_ZS("忽视抗震摄", 70308), 
    OMIT_K_SS("忽视抗三尸-抗吸血", 70309), 
    OMIT_K_GH("忽视抗鬼火-抗哀嚎", 70310), 
    OMIT_K_YW("忽视抗遗忘", 70311), 
    RISE_SPEED("加强加速", 70500), 
    RISE_DEF("加强加防", 70501), 
    RISE_ATTACK("加强加攻", 70502), 
    RISE_PJ("加强破甲", 70503), 
    RISE_ZJ("加强震击", 70504), 
    RISE_ZY("加强治愈", 70505), 
    RISE_QHS("加强横扫", 70506), 
    MAGIC_MISS_RATIO("法术闪躲", 70102), 
    BURN_HURT("伤害加深", 70520), 
    MAGIC_HIT_RATIO("法术命中", 70521), 
    SH_DK("伤害抵抗", 70213);
    
    private String name;
    private int pro;
    
    public static List<Integer> toList() {
        List<Integer> list = Lists.newArrayList();
        for (EQUIP_PRO_DICT n : values()) {
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
    
    private EQUIP_PRO_DICT(final String name, final int pro) {
        this.name = name;
        this.pro = pro;
    }
}
