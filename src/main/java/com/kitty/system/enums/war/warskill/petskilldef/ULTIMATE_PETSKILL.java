
package com.kitty.system.enums.war.warskill.petskilldef;

import java.util.HashMap;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;

public enum ULTIMATE_PETSKILL
{
    PETSKILL_JUEJINGFENGSHENG("绝境逢生", 82001, 1), 
    PETSKILL_ZIXUWUYOU("子虚乌有", 82002, 1), 
    PETSKILL_HUAWU("化无", 82003, 0), 
    PETSKILL_JIANGSI("将死", 82004, 0), 
    PETSKILL_DANGTOUBANGHE("当头棒喝", 82005, 0), 
    PETSKILL_MINGCHAQIUHAO("明察秋毫", 82006, 0), 
    PETSKILL_SHUANGGUANQIXIA("双管齐下", 82007, 0), 
    PETSKILL_ZUONIAOSHOUSAN("作鸟兽散", 82008, 0), 
    PETSKILL_CHUNNUANHUAKAI("春暖花开", 82009, 1), 
    PETSKILL_YIYAHUANYA("以牙还牙", 82010, 0), 
    PETSKILL_CHUNHUIDADI("春回大地", 82011, 1), 
    PETSKILL_DUOHUNSUOMING("夺魂索命", 82012, 1);
    
    private String note;
    private int value;
    private int type;
    
    public static List<Map<String, Integer>> toList() {
        final List list = Lists.newArrayList();
        for (final ULTIMATE_PETSKILL n : values()) {
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
    
    private ULTIMATE_PETSKILL(final String note, final int value, final int type) {
        this.note = note;
        this.value = value;
        this.type = type;
    }
}
