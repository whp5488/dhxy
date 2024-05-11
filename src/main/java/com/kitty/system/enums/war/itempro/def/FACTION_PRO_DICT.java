
package com.kitty.system.enums.war.itempro.def;

import java.util.HashMap;
import java.util.Map;
import com.google.common.collect.Lists;
import java.util.List;

public enum FACTION_PRO_DICT
{
    K_HL("抗混乱", 20200, 0.05), 
    K_DU("抗毒", 20201, 0.05), 
    K_HS("抗昏睡", 20202, 0.05), 
    K_FY("抗封印", 20203, 0.05), 
    K_F("抗风", 20204, 0.05), 
    K_L("抗雷", 20205, 0.05), 
    K_S("抗水", 20206, 0.05), 
    K_H("抗火", 20207, 0.05), 
    K_SS("抗三尸", 20209, 0.05), 
    K_GH("抗鬼火", 20210, 0.05), 
    K_YW("抗遗忘", 20211, 0.05), 
    WL_XS("物理吸收", 20212, 0.04), 
    K_KZ_CAP("抗控制上限", 20214, 0.025), 
    K_TM("抗天魔", 20216, 0.05), 
    K_FG("抗分光", 20217, 0.05), 
    K_QM("抗青面", 20218, 0.05), 
    K_XL("抗小楼", 20219, 0.05);
    
    private String name;
    private int pro;
    private double gain;
    
    public static List<Integer> toList() {
        final List list = Lists.newArrayList();
        for (final FACTION_PRO_DICT n : values()) {
            list.add(n.getPro());
        }
        return list;
    }
    
    public static Map<Integer, Double> toMap() {
        final Map<Integer, Double> map = new HashMap<Integer, Double>();
        for (final FACTION_PRO_DICT n : values()) {
            map.put(n.getPro(), n.getGain());
        }
        return map;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getPro() {
        return this.pro;
    }
    
    public double getGain() {
        return this.gain;
    }
    
    private FACTION_PRO_DICT(final String name, final int pro, final double gain) {
        this.name = name;
        this.pro = pro;
        this.gain = gain;
    }
}
