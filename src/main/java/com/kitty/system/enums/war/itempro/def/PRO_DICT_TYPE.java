
package com.kitty.system.enums.war.itempro.def;

import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public enum PRO_DICT_TYPE
{
    GG("根骨", 1, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    LX("灵性", 2, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    LL("力量", 3, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    MJ("敏捷", 4, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    CZL("成长率", 5, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    HP_CAP("气血上限", 50, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    MP_CAP("法力上限", 51, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    SPEED("速度", 52, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    ATTACK("攻击", 53, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    HP_PCT("气血百分比", 54, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    MP_PCT("法力百分比", 55, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    ATTACK_PCT("攻击百分比", 56, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    SPEED_PCT("速度百分比", 57, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    ATTACK_ADDITION("攻击力加成", 58, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    HIT_RATIO("命中率", 100, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    MISS_RATIO("闪躲率(物理)", 101, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    MAGIC_MISS_RATIO("闪躲率(法术闪躲)", 102, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    COMBO_NUM("连击次数", 103, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    COMBO_RATIO("连击率", 104, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RAGE_RATIO("狂暴率", 105, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    CONQUER_EXTENT("破防程度", 106, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    CONQUER_RATIO("破防概率", 107, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    SHOCK_RATIO("反震率", 108, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    SHOCK_EXTENT("反震程度", 109, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    DEF("防御力", 110, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    CHASE_RATIO("追击概率", 111, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_HL("抗混乱", 200, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_DU("抗毒", 201, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_HS("抗昏睡", 202, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_FY("抗封印-冰封", 203, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_F("抗风", 204, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_L("抗雷", 205, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_S("抗水", 206, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_H("抗火", 207, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_ZS("抗震慑", 208, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_SS("抗三尸-抗吸血", 209, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_GH("抗鬼火-抗哀嚎", 210, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_YW("抗遗忘", 211, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    WL_XS("物理吸收-抗物理", 212, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    SH_DK("伤害抵抗", 213, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_KZ_CAP("抗控制上限", 214, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_MH("抗魅惑", 215, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_TM("抗天魔", 216, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_FG("抗分光", 217, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_QM("抗青面", 218, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    K_XL("抗小楼", 219, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_HL("忽视抗混乱", 300, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_D("忽视抗毒", 301, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_HS("忽视抗昏睡", 302, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_FY("忽视抗封印", 303, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_F("忽视抗风", 304, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_L("忽视抗雷", 305, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_S("忽视抗水", 306, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_H("忽视抗火", 307, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_ZS("忽视抗震摄", 308, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_SS("忽视抗三尸-抗吸血", 309, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_GH("忽视抗鬼火-抗哀嚎", 310, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    OMIT_K_YW("忽视抗遗忘", 311, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    F_KB_RATIO("风系狂暴率", 400, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    L_KB_RATIO("雷系狂暴率", 401, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    S_KB_RATIO("水系狂暴率", 402, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    H_KB_RATIO("火系狂暴率", 403, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    SS_KB_RATIO("三尸狂暴率", 404, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    GH_KB_RATIO("鬼火狂暴率", 405, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    TM_KB_RATIO("天魔狂暴率", 406, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    FG_KB_RATIO("分光狂暴率", 407, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    QM_KB_RATIO("青面狂暴率", 408, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    XL_KB_RATIO("小楼狂暴率", 409, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_SPEED("加强加速", 500, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_DEF("加强加防", 501, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_ATTACK("加强加攻", 502, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_PJ("加强破甲", 503, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_ZJ("加强震击", 504, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_ZY("加强治愈", 505, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_QHS("加强横扫", 506, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_HL("加强混乱", 507, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_D("加强毒", 508, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_HS("加强昏睡", 509, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_FY("加强封印", 510, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_F("加强风", 511, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_L("加强雷", 512, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_S("加强水", 513, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_H("加强火", 514, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_SS("强三尸虫", 515, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_GH("加强鬼火", 516, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_YW("加强遗忘", 517, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_MH("加强魅惑", 518, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    RISE_ZS("加强震慑", 519, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    BURN_HURT("伤害加深", 520, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    MAGIC_HIT_RATIO("法术命中", 521, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    WX_J("金", 600, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    WX_M("木", 601, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    WX_S("水", 602, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    WX_H("火", 603, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    WX_T("土", 604, PRO_TYPE.Pro_Value_NUM_TYPE.getValue()), 
    Q_KJ("强力克金", 605, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    Q_KM("强力克木", 606, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    Q_KS("强力克水", 607, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    Q_KH("强力克火", 608, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()), 
    Q_KT("强力克土", 609, PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue());
    
    private String name;
    private int dist;
    private int type;
    
    public static int getType(final Integer dist) {
        int type = PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue();
        for (final PRO_DICT_TYPE n : values()) {
            if (n.dist == dist) {
                type = n.type;
                break;
            }
        }
        return type;
    }
    
    public static Map<Integer, Integer> toMap() {
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (final PRO_DICT_TYPE n : values()) {
            if (n.getDist() >= PRO_DICT_TYPE.HIT_RATIO.getDist()) {
                map.put(n.getDist(), n.getType());
            }
        }
        return map;
    }
    
    public static Map<Integer, Integer> toMapAll() {
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (final PRO_DICT_TYPE n : values()) {
            map.put(n.getDist(), n.getType());
        }
        return map;
    }
    
    public static List<Integer> getWuXinDist() {
        return Arrays.asList(PRO_DICT_TYPE.WX_J.getDist(), PRO_DICT_TYPE.WX_M.getDist(), PRO_DICT_TYPE.WX_S.getDist(), PRO_DICT_TYPE.WX_H.getDist(), PRO_DICT_TYPE.WX_T.getDist());
    }
    
    public static List<Integer> getQWuXinDist() {
        return Arrays.asList(PRO_DICT_TYPE.Q_KJ.getDist(), PRO_DICT_TYPE.Q_KM.getDist(), PRO_DICT_TYPE.Q_KS.getDist(), PRO_DICT_TYPE.Q_KH.getDist(), PRO_DICT_TYPE.Q_KT.getDist());
    }
    
    public static PRO_DICT_TYPE getProDictTypeByDist(final int dist) {
        for (final PRO_DICT_TYPE obj : values()) {
            if (obj.getDist() == dist) {
                return obj;
            }
        }
        return null;
    }
    
    public static Map<Integer, List<Integer>> getGFSDict() {
        final Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        map.put(1008, Arrays.asList(PRO_DICT_TYPE.HIT_RATIO.getDist(), PRO_DICT_TYPE.RISE_ATTACK.getDist()));
        map.put(1009, Arrays.asList(PRO_DICT_TYPE.K_FY.getDist(), PRO_DICT_TYPE.K_HS.getDist(), PRO_DICT_TYPE.K_HL.getDist(), PRO_DICT_TYPE.K_YW.getDist(), PRO_DICT_TYPE.K_F.getDist(), PRO_DICT_TYPE.K_L.getDist(), PRO_DICT_TYPE.K_S.getDist(), PRO_DICT_TYPE.K_H.getDist(), PRO_DICT_TYPE.K_SS.getDist(), PRO_DICT_TYPE.K_GH.getDist(), PRO_DICT_TYPE.K_DU.getDist(), PRO_DICT_TYPE.WL_XS.getDist()));
        map.put(1010, Arrays.asList(PRO_DICT_TYPE.RISE_SPEED.getDist()));
        return map;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getDist() {
        return this.dist;
    }
    
    public int getType() {
        return this.type;
    }
    
    private PRO_DICT_TYPE(final String name, final int dist, final int type) {
        this.name = name;
        this.dist = dist;
        this.type = type;
    }
}
