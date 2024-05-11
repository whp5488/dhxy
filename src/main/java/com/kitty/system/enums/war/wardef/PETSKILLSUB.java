
package com.kitty.system.enums.war.wardef;

public enum PETSKILLSUB
{
    SUBTYPE_NORMAL("普通技能", 0), 
    SUBTYPE_SENIOR("高级技能", 1), 
    SUBTYPE_SUPREME("终极技能", 2), 
    SUBTYPE_BORN("天生技能", 3), 
    SUBTYPE_SPECIAL("神兽技能", 4), 
    SUB_TYPE_SOUL("灵兽技能", 5);
    
    private String note;
    private int value;
    
    public static String getNote(final int value) {
        String note = PETSKILLSUB.SUBTYPE_NORMAL.note;
        for (final PETSKILLSUB n : values()) {
            if (n.value == value) {
                note = n.note;
                break;
            }
        }
        return note;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private PETSKILLSUB(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
