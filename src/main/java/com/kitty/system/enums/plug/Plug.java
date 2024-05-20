
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
        final List list = Lists.newArrayList();
        for (final Plug n : values()) {
            list.add(n.getSerial());
        }
        return list;
    }
    
    public static String getTips(final int serial) {
        for (final Plug n : values()) {
            if (serial == n.getSerial()) {
                return n.getTips();
            }
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
