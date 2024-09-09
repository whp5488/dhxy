package com.kitty.system.enums.war.itempro.def;

public enum ITEM_LARGE_SON_TYPE
{
    ITEM_DEF_TYPE_SKILL_BOOK("技能书", 2);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private ITEM_LARGE_SON_TYPE(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
