
package com.kitty.system.enums.war.itempro.def;

import com.google.common.collect.Lists;
import java.util.List;

public enum CONVERT_DICT
{
    RISE_ATTACK("加强加攻", "AdAtkEhan", PRO_DICT_TYPE.RISE_ATTACK.getDist()), 
    RISE_DEF("加强加防", "AdDefEhan", PRO_DICT_TYPE.RISE_DEF.getDist()), 
    RISE_SPEED("加强加速", "AdSpdEhan", PRO_DICT_TYPE.RISE_SPEED.getDist()), 
    Atk("攻击", "Atk", PRO_DICT_TYPE.ATTACK.getDist()), 
    ATTACK_PCT("攻击百分比", "AtkPercent", PRO_DICT_TYPE.ATTACK_PCT.getDist()), 
    COMBO_NUM("连击次数", "HitCombo", PRO_DICT_TYPE.COMBO_NUM.getDist()), 
    SPEED("速度", "Speed", PRO_DICT_TYPE.SPEED.getDist()), 
    MJ("敏捷", "Agility", PRO_DICT_TYPE.MJ.getDist()), 
    GG("根骨", "Basecon", PRO_DICT_TYPE.GG.getDist()), 
    LL("力量", "Power", PRO_DICT_TYPE.LL.getDist()), 
    LX("灵性", "Wakan", PRO_DICT_TYPE.LX.getDist()), 
    RISE_PJ("加强破甲", "BreakEhan", PRO_DICT_TYPE.RISE_PJ.getDist()), 
    RISE_ZY("加强治愈", "CureEhan", PRO_DICT_TYPE.RISE_ZY.getDist()), 
    K_HL("抗混乱", "DisorderDef", PRO_DICT_TYPE.K_HL.getDist()), 
    K_H("抗火", "FireDef", PRO_DICT_TYPE.K_H.getDist()), 
    OMIT_K_H("忽视抗火", "FireDefNeg", PRO_DICT_TYPE.OMIT_K_H.getDist()), 
    K_YW("抗遗忘", "ForgetDef", PRO_DICT_TYPE.K_YW.getDist()), 
    OMIT_K_YW("忽视抗遗忘", "ForgetDefNeg", PRO_DICT_TYPE.OMIT_K_YW.getDist()), 
    K_GH("抗鬼火", "GfireDef", PRO_DICT_TYPE.K_GH.getDist()), 
    OMIT_K_HL("忽视抗混", "DisorderDefNeg", PRO_DICT_TYPE.OMIT_K_HL.getDist()), 
    OMIT_K_GH("忽视抗鬼火", "GfireDefNeg", PRO_DICT_TYPE.OMIT_K_GH.getDist()), 
    COMBO_RATIO("连击率", "HitComboRate", PRO_DICT_TYPE.COMBO_RATIO.getDist()), 
    HIT_RATIO("命中率", "HitRate", PRO_DICT_TYPE.HIT_RATIO.getDist()), 
    HP_CAP("气血上限", "HpMax", PRO_DICT_TYPE.HP_CAP.getDist()), 
    HP_PCT("气血百分比", "HpPercent", PRO_DICT_TYPE.HP_PCT.getDist()), 
    HP_PCT_CARD("气血百分比", "HpCardPercent", PRO_DICT_TYPE.HP_PCT.getDist()), 
    MP_CAP("法力上限", "MpMax", PRO_DICT_TYPE.MP_CAP.getDist()), 
    MP_PCT("法力百分比", "MpPercent", PRO_DICT_TYPE.MP_PCT.getDist()), 
    MP_PCT_CARD("法力百分比", "MpCardPercent", PRO_DICT_TYPE.MP_PCT.getDist()), 
    WL_XS("物理吸收", "PhyDef", PRO_DICT_TYPE.WL_XS.getDist()), 
    CONQUER_EXTENT("破防程度", "PhyDefNef", PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), 
    CONQUER_RATIO("破防概率", "PhyDefNefRate", PRO_DICT_TYPE.CONQUER_RATIO.getDist()), 
    K_DU("抗毒", "PoisonDef", PRO_DICT_TYPE.K_DU.getDist()), 
    OMIT_K_D("忽视抗毒", "PoisonDefNeg", PRO_DICT_TYPE.OMIT_K_D.getDist()), 
    K_S("抗水", "RainDef", PRO_DICT_TYPE.K_S.getDist()), 
    OMIT_K_S("忽视抗水", "RainDefNeg", PRO_DICT_TYPE.OMIT_K_S.getDist()), 
    K_SS("抗三尸", "SanshiDef", PRO_DICT_TYPE.K_SS.getDist()), 
    OMIT_K_SS("忽视抗三尸", "SanshiDefNeg", PRO_DICT_TYPE.OMIT_K_SS.getDist()), 
    K_FY("抗封印", "SealDef", PRO_DICT_TYPE.K_FY.getDist()), 
    OMIT_K_FY("忽视抗封", "SealDefNeg", PRO_DICT_TYPE.OMIT_K_FY.getDist()), 
    K_ZS("抗震慑", "ShockDef", PRO_DICT_TYPE.K_ZS.getDist()), 
    OMIT_K_ZS("忽视抗震摄", "ShockDefNeg", PRO_DICT_TYPE.OMIT_K_ZS.getDist()), 
    K_HS("抗昏睡", "SleepDef", PRO_DICT_TYPE.K_HS.getDist()), 
    OMIT_K_HS("忽视抗睡", "SleepDefNeg", PRO_DICT_TYPE.OMIT_K_HS.getDist()), 
    SPEED_PCT("速度百分比", "SpeedPercent", PRO_DICT_TYPE.SPEED_PCT.getDist()), 
    SPEED_PCT_CARD("速度百分比", "SpeedCardPercent", PRO_DICT_TYPE.SPEED_PCT.getDist()), 
    RISE_QHS("加强横扫", "SweepEhan", PRO_DICT_TYPE.RISE_QHS.getDist()), 
    RISE_ZJ("加强震击", "ThudEhan", PRO_DICT_TYPE.RISE_ZJ.getDist()), 
    RISE_MH("加强魅惑", "CharmEhan", PRO_DICT_TYPE.RISE_MH.getDist()), 
    RISE_S("加强水", "RainEhan", PRO_DICT_TYPE.RISE_S.getDist()), 
    K_L("抗雷", "ThunderDef", PRO_DICT_TYPE.K_L.getDist()), 
    OMIT_K_L("忽视抗雷", "ThunderDefNeg", PRO_DICT_TYPE.OMIT_K_L.getDist()), 
    MISS_RATIO("闪躲率", "VoidRate", PRO_DICT_TYPE.MISS_RATIO.getDist()), 
    K_F("抗风", "WindDef", PRO_DICT_TYPE.K_F.getDist()), 
    OMIT_K_F("忽视抗风", "WindDefNeg", PRO_DICT_TYPE.OMIT_K_F.getDist()), 
    RAGE_RATIO("狂暴率", "FatalRate", PRO_DICT_TYPE.RAGE_RATIO.getDist()), 
    H_KB_RATIO("火系狂暴率", "FireRageOdds", PRO_DICT_TYPE.H_KB_RATIO.getDist()), 
    GH_KB_RATIO("鬼火狂暴率", "GfireRageOdds", PRO_DICT_TYPE.GH_KB_RATIO.getDist()), 
    S_KB_RATIO("水系狂暴率", "RainRageOdds", PRO_DICT_TYPE.S_KB_RATIO.getDist()), 
    SS_KB_RATIO("三尸狂暴率", "ShanshiRageOdds", PRO_DICT_TYPE.SS_KB_RATIO.getDist()), 
    L_KB_RATIO("雷系狂暴率", "ThunderRageOdds", PRO_DICT_TYPE.L_KB_RATIO.getDist()), 
    F_KB_RATIO("风系狂暴率", "WindRageOdds", PRO_DICT_TYPE.F_KB_RATIO.getDist()), 
    Q_KJ("强力克金", "ConGold", PRO_DICT_TYPE.Q_KJ.getDist()), 
    Q_KM("强力克木", "ConWood", PRO_DICT_TYPE.Q_KM.getDist()), 
    Q_KS("强力克水", "ConWater", PRO_DICT_TYPE.Q_KS.getDist()), 
    Q_KH("强力克火", "ConFire", PRO_DICT_TYPE.Q_KH.getDist()), 
    Q_KT("强力克土", "ConSoil", PRO_DICT_TYPE.Q_KT.getDist()), 
    MAGIC_MISS_RATIO("法术闪躲", "SpellVoidRate", PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist()), 
    BURN_HURT("伤害加深", "DamageIncrease", PRO_DICT_TYPE.BURN_HURT.getDist()), 
    MAGIC_HIT_RATIO("法术命中", "SpellHitRate", PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), 
    SH_DK("伤害抵抗", "DamageReduce", PRO_DICT_TYPE.SH_DK.getDist()), 
    SHOCK_EXTENT("反震程度", "ReshockLevel", PRO_DICT_TYPE.SHOCK_EXTENT.getDist()), 
    RISE_D("加强毒", "PoisonEhan", PRO_DICT_TYPE.RISE_D.getDist()), 
    RISE_HL("加强混乱", "DisorderEhan", PRO_DICT_TYPE.RISE_HL.getDist()), 
    RISE_YW("加强遗忘", "ForgetEhan", PRO_DICT_TYPE.RISE_YW.getDist()), 
    SHOCK_RATIO("反震率", "ReshockRate", PRO_DICT_TYPE.SHOCK_RATIO.getDist()), 
    RISE_SS("强三尸虫", "SanshiEhan", PRO_DICT_TYPE.RISE_SS.getDist()), 
    RISE_FY("加强封印", "SealEhan", PRO_DICT_TYPE.RISE_FY.getDist()), 
    RISE_F("加强风", "WindEhan", PRO_DICT_TYPE.RISE_F.getDist()), 
    RISE_L("加强雷", "ThunderEhan", PRO_DICT_TYPE.RISE_L.getDist()), 
    RISE_ZS("加强震慑", "ShockEhan", PRO_DICT_TYPE.RISE_ZS.getDist()), 
    RISE_HS("加强昏睡", "SleepEhan", PRO_DICT_TYPE.RISE_HS.getDist()), 
    RISE_H("加强火", "FireEhan", PRO_DICT_TYPE.RISE_H.getDist()), 
    RISE_GH("加强鬼火", "GfireEhan", PRO_DICT_TYPE.RISE_GH.getDist()), 
    ATTACK_PCT_CARD("攻击百分比", "AtkCardPercent", PRO_DICT_TYPE.ATTACK_PCT.getDist()), 
    WX_J("金", "Gold", PRO_DICT_TYPE.WX_J.getDist()), 
    WX_M("木", "Wood", PRO_DICT_TYPE.WX_M.getDist()), 
    WX_S("水", "Water", PRO_DICT_TYPE.WX_S.getDist()), 
    WX_H("火", "Fire", PRO_DICT_TYPE.WX_H.getDist()), 
    WX_T("土", "Soil", PRO_DICT_TYPE.WX_T.getDist());
    
    private String name;
    private String dist;
    private int pro;
    
    public static List<Integer> toList() {
        final List list = Lists.newArrayList();
        for (final CONVERT_DICT n : values()) {
            list.add(n.getPro());
        }
        return list;
    }
    
    public static int getDist(final String dist) {
        int pro = -1;
        for (final CONVERT_DICT n : values()) {
            if (n.dist.equals(dist)) {
                pro = n.pro;
                break;
            }
        }
        return pro;
    }
    
    public static String getName(final String dist) {
        String name = null;
        for (final CONVERT_DICT n : values()) {
            if (n.dist.equals(dist)) {
                name = n.getName();
                break;
            }
        }
        return name;
    }
    
    public static String getPro(final int pro) {
        String dist = null;
        for (final CONVERT_DICT n : values()) {
            if (pro == n.pro) {
                dist = n.dist;
                break;
            }
        }
        return dist;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDist() {
        return this.dist;
    }
    
    public int getPro() {
        return this.pro;
    }
    
    private CONVERT_DICT(final String name, final String dist, final int pro) {
        this.name = name;
        this.dist = dist;
        this.pro = pro;
    }
}
