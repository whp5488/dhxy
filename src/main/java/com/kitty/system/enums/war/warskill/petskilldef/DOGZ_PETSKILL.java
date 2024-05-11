
package com.kitty.system.enums.war.warskill.petskilldef;

import java.util.HashMap;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;

public enum DOGZ_PETSKILL
{
    PETSKILL_QIANGHUAXUANREN("强化悬刃", 83001, 0), 
    PETSKILL_QIANGHUAYIHUAN("强化遗患", 83002, 0), 
    PETSKILL_BINGLINCHENGXIA("兵临城下", 83003, 1), 
    PETSKILL_RUHUTIANYI("如虎添翼", 83004, 0), 
    PETSKILL_NIEPAN("涅槃--重生", 83005, 0), 
    PETSKILL_JINGGUANBAIRI("精贯白日", 83006, 0), 
    PETSKILL_CHAOMINGDIANCHE("潮鸣电掣", 83007, 0);
    
    private String note;
    private int value;
    private int type;
    
    public static List<Map<String, Integer>> toList() {
        final List list = Lists.newArrayList();
        for (final DOGZ_PETSKILL n : values()) {
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
    
    private DOGZ_PETSKILL(final String note, final int value, final int type) {
        this.note = note;
        this.value = value;
        this.type = type;
    }
}
