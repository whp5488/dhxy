package com.kitty.system.enums.common;

public enum ACC_GRADE
{
    PLAY_WITH("把玩", (byte)2), 
    RESERVE("珍藏", (byte)3), 
    PRICELESS("无价", (byte)4), 
    TRENDS("传世", (byte)5);
    
    private String note;
    private byte value;
    
    public String getNote() {
        return this.note;
    }
    
    public byte getValue() {
        return this.value;
    }
    
    private ACC_GRADE(final String note, final byte value) {
        this.note = note;
        this.value = value;
    }
}
