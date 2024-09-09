package com.kitty.system.enums.common;

public enum TRAIN
{
    QIAN("乾位", 1), 
    XUN("巽位", 2), 
    KAN("坎位", 3), 
    GEN("艮位", 4), 
    KUN("坤位", 5), 
    ZHEN("震位", 6), 
    LI("离位", 7), 
    DUI("兑位", 8);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private TRAIN(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
