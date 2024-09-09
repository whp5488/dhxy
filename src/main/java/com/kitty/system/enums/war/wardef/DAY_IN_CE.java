package com.kitty.system.enums.war.wardef;

public enum DAY_IN_CE
{
    GATTACA("千钧", (byte)1), 
    LOAD_OBJ("载物", (byte)2), 
    DRIVER_BEASTS("御兽", (byte)3), 
    PRODUCT_ORDER_ONE("把玩", (byte)1), 
    PRODUCT_ORDER_TWO("珍藏", (byte)2), 
    PRODUCT_ORDER_THREE("无价", (byte)3);
    
    private String note;
    private byte value;
    
    public String getNote() {
        return this.note;
    }
    
    public byte getValue() {
        return this.value;
    }
    
    private DAY_IN_CE(final String note, final byte value) {
        this.note = note;
        this.value = value;
    }
}
