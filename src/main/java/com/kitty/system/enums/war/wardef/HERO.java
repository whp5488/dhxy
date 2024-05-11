
package com.kitty.system.enums.war.wardef;

public enum HERO
{
    HERO_ANY("性别不限", 0), 
    HERO_MALE("男性英雄", 1), 
    HERO_FEMALE("女性英雄", 2);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private HERO(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
