
package com.kitty.system.enums.war.warskill.skilldef;

import java.util.HashMap;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;

public enum EFFECTTYPE
{
    EFFECTTYPE_NONE("无效果", 0, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_CONFUSE("混乱", 40001, 0, 1, 0, 1, 1, 1, 1, 1, "0"), 
    EFFECTTYPE_FROZEN("封印（冰封）", 40002, 0, 0, 0, 1, 1, 1, 1, 1, "0"), 
    EFFECTTYPE_SLEEP("昏睡", 40003, 0, 1, 0, 1, 1, 1, 1, 1, "0"), 
    EFFECTTYPE_POISON("中毒", 40004, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_CONFUSE_OFF("混乱解除", 45001, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FROZEN_OFF("封印（冰封）解除", 45002, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SLEEP_OFF("昏睡解除", 45003, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_POISON_OFF("中毒解除", 45004, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_SPEED("速度提升", 40101, 1, 1, 0, 0, 0, 0, 0, 0, "{1,PROPERTY_SP}"), 
    EFFECTTYPE_ADV_DAMAGE("伤害提升", 40102, 1, 1, 0, 0, 0, 0, 0, 0, "{1,PROPERTY_AP}"), 
    EFFECTTYPE_ADV_WULI("抗物理提升", 40103, 1, 1, 0, 0, 0, 0, 0, 0, "{2,SKILLATTR_WULI}"), 
    EFFECTTYPE_ADV_RENZU("抗人族提升", 40104, 1, 1, 0, 0, 0, 0, 0, 0, "{2,{SKILLATTR_POISON,SKILLATTR_SLEEP,SKILLATTR_CONFUSE,SKILLATTR_ICE}}"), 
    EFFECTTYPE_ADV_XIANZU("抗仙族提升", 40105, 1, 1, 0, 0, 0, 0, 0, 0, "{2,{SKILLATTR_FIRE,SKILLATTR_WIND,SKILLATTR_THUNDER,SKILLATTR_WATER}}"), 
    EFFECTTYPE_ADV_MINGZHONG("命中提升", 40106, 1, 1, 0, 0, 0, 0, 0, 0, "{1,PROPERTY_PACC}"), 
    EFFECTTYPE_ADV_DEFEND("进入防御", 40107, 0, 1, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DEC_WULI("抗物理降低", 40108, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_DEC_RENZU("抗人族降低", 40109, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_DEC_XIANZU("抗仙族降低", 40110, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_DEC_ZHEN("抗虹吸降低", 40111, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_DEC_SPEED("速度降低", 40112, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHOUHUCANGSHENG("守护苍生", 40113, 0, 1, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHUAIRUO("衰弱", 40114, 0, 1, 1, 1, 1, 0, 0, 0, "{2,{SKILLATTR_ZHEN,SKILLATTR_YIWANG,SKILLATTR_AIHAO,SKILLATTR_XIXUE}}"), 
    EFFECTTYPE_YIWANG("遗忘", 40115, 0, 1, 0, 1, 1, 1, 1, 0, "{1,PROPERTY_YIWANGSKILL}"), 
    EFFECTTYPE_ADV_NIAN("年增益buff", 40116, 1, 1, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_SPEED_OFF("速度提升解除", 45101, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_DAMAGE_OFF("伤害提升解除", 45102, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_WULI_OFF("抗物理提升解除", 45103, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_RENZU_OFF("抗人族提升解除", 45104, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_XIANZU_OFF("抗仙族提升解除", 45105, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_MINGZHONG_OFF("命中提升解除", 45106, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_DEFEND_OFF("进入防御解除", 45107, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DEC_WULI_OFF("抗物理降低解除", 45108, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DEC_RENZU_OFF("抗人族降低解除", 45109, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DEC_XIANZU_OFF("抗仙族降低解除", 45110, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DEC_ZHEN_OFF("抗虹吸降低解除", 45111, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DEC_SPEED_OFF("速度降低解除", 45112, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHOUHUCANGSHENG_OFF("守护苍生解除", 45113, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHUAIRUO_OFF("衰弱解除", 45114, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_YIWANG_OFF("遗忘解除", 45115, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADV_NIAN_OFF("年增益buff", 45116, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FURY_OFF("狂暴解除", 45201, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_RUHUTIANYI_OFF("如虎添翼解除", 45228, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHUNSHUITUIZHOU_OFF("顺水推舟解除(恐惧)", 45275, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_HENGYUNDUANFENG_OFF("横云断峰解除(巫毒咒)", 45273, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHUSHOUWUCE_OFF("束手无策解除(衰弱)", 45274, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LONGZHANYUYE_OFF("龙战于野解除", 45238, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_STEALTH_OFF("隐身解除", 45240, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DUOHUNSUOMING_OFF("夺魂索命解除", 45241, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_OFF("宠物五行技能解除", 45244, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_JIN_OFF("宠物五行技能解除(金)", 45245, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_MU_OFF("宠物五行技能解除(木)", 45246, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_SHUI_OFF("宠物五行技能解除(水)", 45247, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_HUO_OFF("宠物五行技能解除(火)", 45248, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_TU_OFF("宠物五行技能解除(土)", 45249, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_TONGCHOUDIKAI_OFF("同仇敌忾解除", 45250, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_MONSTER_WUDI_OFF("怪物无敌(主怪无敌、小怪无敌)解除", 45252, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FENGMO_OFF("封魔解除", 45253, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FURY("狂暴", 40201, 0, 1, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FATALLY("致命", 40202, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DOUBLEHIT("连击", 40203, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_COUNTERATTACK("反击", 40204, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_REVERBERATE("反震", 40205, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_MISS("闪避", 40206, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DOUBLEHIT_FINAL("连击结束", 40207, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_REVERBERATE_COME("即将引起反震", 40208, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_COUNTERATTACK_COME("即将引起反击", 40209, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_USEDRUG_HP("使用生命药水", 40210, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_USEDRUG_MP("使用魔法药水", 40211, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DOUBLEHIT_COME("即将触发连击", 40212, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DAMAGESELF_HP("自残生命", 40213, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DAMAGESELF_MP("自残魔法", 40214, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_GESHANDANIU("隔山打牛", 40215, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WANFOCHAOZONG("万佛朝宗", 40216, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_IMMUNITY("免疫", 40217, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_RUNAWAY("逃跑", 40218, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACKMP("魔法不足", 40219, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACKITEM("药品不足", 40220, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LIUANHUAMING("柳暗花明", 40221, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FULUSHUANGQUAN("福禄双全", 40222, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_PURSUIT("追击", 40223, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_JIRENTIANXIANG("吉人天相", 40224, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHUNSHUITUIZHOU("顺水推舟(恐惧)", 40275, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_LACKHP("气血不足", 40226, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_BINGLINCHENGXIA("兵临城下", 40227, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_RUHUTIANYI("如虎添翼", 40228, 0, 1, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_JINGGUANBAIRI("精贯白日", 40229, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACKGG("根骨不足", 40230, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACKLX("灵性不足", 40231, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACKMJ("敏捷不足", 40232, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACKLL("力量不足", 40233, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_JUMPHIT("跳跃连击", 40234, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_HENGYUNDUANFENG("横云断峰(巫毒咒)", 40273, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_SHUSHOUWUCE("束手无策(衰弱)", 40274, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_INVALID("无效", 40237, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LONGZHANYUYE("龙战于野", 40238, 0, 1, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_LACKHUOLI("活力不足", 40239, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_STEALTH("隐身", 40240, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_DUOHUNSUOMING("夺魂索命", 40241, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ADDHPFAILED_DHSM("夺魂索命导致加血失败", 40242, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FENSHEN("分身", 40243, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING("宠物五行技能", 40244, 1, 0, 1, 1, 1, 0, 0, 1, "0"), 
    EFFECTTYPE_WUXING_JIN("宠物五行技能(金)", 40245, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_MU("宠物五行技能(木)", 40246, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_SHUI("宠物五行技能(水)", 40247, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_HUO("宠物五行技能(火)", 40248, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_WUXING_TU("宠物五行技能(土)", 40249, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_TONGCHOUDIKAI("同仇敌忾", 40250, 0, 1, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_IMMUNITY_DAMAGE("伤害免疫", 40251, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_MONSTER_WUDI("怪物无敌(主怪无敌、小怪无敌)", 40252, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_FENGMO("封魔", 40253, 0, 1, 0, 1, 1, 1, 1, 1, "0"), 
    EFFECTTYPE_LACK_WXJIN("五行金不足", 40254, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACK_WXMU("五行木不足", 40255, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACK_WXSHUI("五行水不足", 40256, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACK_WXHUO("五行火不足", 40257, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_LACK_WXTU("五行土不足", 40258, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_NOSKILLANI("不播放技能特效", 40259, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_SHAREDAMAGE("庇护", 40260, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_CHUNHUIDADI("春回大地", 40261, 0, 0, 0, 0, 0, 0, 0, 0, "0"), 
    EFFECTTYPE_ESCAPE("逃生", 40262, 0, 0, 0, 0, 0, 0, 0, 0, "0");
    
    private String note;
    private int value;
    private int swbl;
    private int fyqc;
    private int mbshClear;
    private int jsClear;
    private int dtbhClear;
    private int chddClear;
    private int gyzlControl;
    private int qsshClear;
    private String yhjy;
    
    public static List<Map<String, Object>> toList() {
        final List list = Lists.newArrayList();
        for (final EFFECTTYPE effecttype : values()) {
            final Map<String, Object> map = new HashMap<String, Object>();
            map.put("value", effecttype.getValue());
            map.put("swbl", effecttype.getSwbl());
            map.put("fyqc", effecttype.getFyqc());
            map.put("mbshClear", effecttype.getMbshClear());
            map.put("jsClear", effecttype.getJsClear());
            map.put("dtbhClear", effecttype.getDtbhClear());
            map.put("chddClear", effecttype.getChddClear());
            map.put("gyzlControl", effecttype.getGyzlControl());
            map.put("qsshClear", effecttype.getQsshClear());
            map.put("yhjy", effecttype.getYhjy());
            list.add(map);
        }
        return list;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public int getSwbl() {
        return this.swbl;
    }
    
    public int getFyqc() {
        return this.fyqc;
    }
    
    public int getMbshClear() {
        return this.mbshClear;
    }
    
    public int getJsClear() {
        return this.jsClear;
    }
    
    public int getDtbhClear() {
        return this.dtbhClear;
    }
    
    public int getChddClear() {
        return this.chddClear;
    }
    
    public int getGyzlControl() {
        return this.gyzlControl;
    }
    
    public int getQsshClear() {
        return this.qsshClear;
    }
    
    public String getYhjy() {
        return this.yhjy;
    }
    
    private EFFECTTYPE(final String note, final int value, final int swbl, final int fyqc, final int mbshClear, final int jsClear, final int dtbhClear, final int chddClear, final int gyzlControl, final int qsshClear, final String yhjy) {
        this.note = note;
        this.value = value;
        this.swbl = swbl;
        this.fyqc = fyqc;
        this.mbshClear = mbshClear;
        this.jsClear = jsClear;
        this.dtbhClear = dtbhClear;
        this.chddClear = chddClear;
        this.gyzlControl = gyzlControl;
        this.qsshClear = qsshClear;
        this.yhjy = yhjy;
    }
}
