
package com.kitty.system.enums.war.wardef;

public enum RACE
{
    RACE_REN("人族", 1), 
    RACE_XIAN("仙族", 2), 
    RACE_MO("魔族", 3), 
    RACE_GUI("鬼族", 4), 
    RACE_LONG("龙族", 5);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private RACE(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
