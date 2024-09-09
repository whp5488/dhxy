package com.kitty.system.enums.plug;

import org.apache.commons.compress.utils.Lists;
import java.util.List;

public enum Plug
{
    TREASURE_MAP(2000, "plug_treasure", "挖宝"), 
    TREASURE_MAP_BUY(2001, null, null);
    
    private int serial;
    private String dict;
    private String tips;
    
    public static List<Integer> toList() {
        List<Integer> list = Lists.newArrayList();
        for (Plug n : values()) {
            list.add(Integer.valueOf(n.getSerial()));
        }
        return list;
    }
    
    public static String getTips(int serial) {
        Plug[] values = values();
        int length = values.length;
        int i = 0;
        while (i < length) {
            Plug n = values[i];
            if (serial == n.getSerial()) {
                return n.getTips();
            }
            ++i;
        }
        return null;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public String getTips() {
        return this.tips;
    }
    
    private Plug(final int serial, final String dict, final String tips) {
        this.serial = serial;
        this.dict = dict;
        this.tips = tips;
    }
}
