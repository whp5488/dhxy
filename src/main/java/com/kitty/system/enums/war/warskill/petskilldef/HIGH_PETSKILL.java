
package com.kitty.system.enums.war.warskill.petskilldef;

import java.util.HashMap;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;

public enum HIGH_PETSKILL
{
    PETSKILL_FUSHANG("扶伤", 81001, 0), 
    PETSKILL_LANGYUEQINGFENG("凝神诀", 81002, 0), 
    PETSKILL_YIHUAN("遗患", 81003, 0), 
    PETSKILL_XUANREN("悬刃", 81004, 0), 
    PETSKILL_GAOJIQINGMINGSHU("高级清明术", 81005, 0), 
    PETSKILL_GAOJITUOKUNSHU("高级脱困术", 81006, 0), 
    PETSKILL_GAOJIFENLIEGONGJI("高级分裂攻击", 81007, 0), 
    PETSKILL_GAOJICIWUFANBU("高级慈乌反哺", 81008, 0), 
    PETSKILL_GAOJIFANBUZHISI("高级反哺之私", 81009, 0), 
    PETSKILL_RENLAIFENG("人来疯", 81010, 0), 
    PETSKILL_TAOMING("讨命", 81011, 0), 
    PETSKILL_HUIYUAN("回源", 81012, 0), 
    PETSKILL_BAOFU("报复", 81013, 0), 
    PETSKILL_FEIYANHUIXIANG("飞燕回翔", 81014, 1), 
    PETSKILL_YINGJICHANGKONG("鹰击长空", 81015, 0), 
    PETSKILL_BUBUSHENGLIAN("步步生莲", 81016, 1), 
    PETSKILL_LIUANHUAMING("柳暗花明", 81017, 0), 
    PETSKILL_JINTUIZIRU("进退自如", 81018, 0), 
    PETSKILL_LONGZHANYUYE("龙战于野", 81019, 1), 
    PETSKILL_HENGYUNDUANFENG("横云断峰(巫毒咒)", 81020, 1), 
    PETSKILL_SHUSHOUWUCE("束手无策", 81021, 1), 
    PETSKILL_SHUNSHUITUIZHOU("顺水推舟(恐惧)", 81022, 0), 
    PETSKILL_NIANHUAYIXIAO("拈花一笑", 81023, 1), 
    PETSKILL_FENHUAFULIU("分花拂柳", 81024, 0), 
    PETSKILL_FULUSHUANGQUAN("福禄双全", 81025, 0), 
    PETSKILL_JIRENTIANXIANG("吉人天相", 81026, 0), 
    PETSKILL_MIAOBISHENGHUA("妙笔生花", 81027, 1), 
    PETSKILL_JINYUZHOU("金玉咒", 81028, 1), 
    PETSKILL_XIUMUZHOU("朽木咒", 81029, 1), 
    PETSKILL_LIUSHUIZHOU("流水咒", 81030, 1), 
    PETSKILL_LIEYANZHOU("烈焰咒", 81031, 1), 
    PETSKILL_LIETUZHOU("裂土咒", 81032, 1), 
    PETSKILL_TIESHUKAIHUA("铁树开花", 81033, 1), 
    PETSKILL_ZHINANERTUI("知难而退", 81034, 0), 
    PETSKILL_SHUNSHOUQIANYANG("顺手牵羊", 81035, 0), 
    PETSKILL_DAOZHUANQIANKUN("倒转乾坤", 81036, 0), 
    PETSKILL_TANHUAYIXIAN("昙花一现", 81037, 0), 
    PETSKILL_HUICHUNMIAOSHOU("回春妙手", 81038, 1), 
    PETSKILL_JINLINGHUTI("金灵护体", 81039, 0), 
    PETSKILL_MULINGHUTI("木灵护体", 81040, 0), 
    PETSKILL_TULINGHUTI("土灵护体", 81041, 0), 
    PETSKILL_SHUILINGHUTI("水灵护体", 81042, 0), 
    PETSKILL_HUOLINGHUTI("火灵护体", 81043, 0), 
    PETSKILL_FENGMO("封魔", 81044, 1), 
    PETSKILL_GAOJIJISHIYU("高级使命召唤", 81045, 0), 
    PETSKILL_GAOJININGSHENSHU("高级凝神术", 81046, 0), 
    PETSKILL_TUXI("突袭", 81047, 0), 
    PETSKILL_QIANFU("潜伏", 81048, 1);
    
    private String note;
    private int value;
    private int type;
    
    public static List<Map<String, Integer>> toList() {
        final List list = Lists.newArrayList();
        for (final HIGH_PETSKILL n : values()) {
            final Map<String, Integer> map = new HashMap<String, Integer>();
            map.put("value", n.getValue());
            map.put("type", n.getType());
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
    
    public int getType() {
        return this.type;
    }
    
    private HIGH_PETSKILL(final String note, final int value, final int type) {
        this.note = note;
        this.value = value;
        this.type = type;
    }
}
