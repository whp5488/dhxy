package com.kitty.system.enums.war.itempro.def;

public enum PRO_TYPE
{
    Pro_Value_NUM_TYPE("直接数值的，（整数）", 0), 
    Pro_Value_PERCENT_TYPE("有百分号的（小数显示后一位）", 1), 
    Pro_Value_CZL_TYPE("直接数值的，（小数点后4位）", 2);
    
    private String note;
    private int value;
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    private PRO_TYPE(final String note, final int value) {
        this.note = note;
        this.value = value;
    }
}
