
package com.kitty.system.enums.war.wardef;

public enum WarInstruct
{
    OWA((short)0, (byte)5, (byte)6), 
    ORDER_REN_M((short)1, (byte)3, (byte)6), 
    ORDER_REN_G((short)2, (byte)3, (byte)6), 
    ORDER_XIAN_M((short)3, (byte)3, (byte)6), 
    ORDER_XIAN_G((short)4, (byte)3, (byte)6), 
    ORDER_MO_M((short)5, (byte)3, (byte)6), 
    ORDER_MO_G((short)6, (byte)3, (byte)6), 
    ORDER_GUI_M((short)7, (byte)3, (byte)6), 
    ORDER_GUI_G((short)8, (byte)3, (byte)6), 
    ORDER_LONG_M((short)9, (byte)3, (byte)6), 
    ORDER_LONG_G((short)10, (byte)3, (byte)6), 
    ORDER_PET((short)99, (byte)3, (byte)6), 
    ASSIGN_OWA((short)1000, (byte)3, (byte)4), 
    ASSIGN_REN_M((short)1001, (byte)3, (byte)4), 
    ASSIGN_REN_G((short)1002, (byte)3, (byte)4), 
    ASSIGN_XIAN_M((short)1003, (byte)3, (byte)4), 
    ASSIGN_XIAN_G((short)1004, (byte)3, (byte)4), 
    ASSIGN_MO_M((short)1005, (byte)3, (byte)4), 
    ASSIGN_MO_G((short)1006, (byte)3, (byte)4), 
    ASSIGN_GUI_M((short)1007, (byte)3, (byte)4), 
    ASSIGN_GUI_G((short)1008, (byte)3, (byte)4), 
    ASSIGN_LONG_M((short)1009, (byte)3, (byte)4), 
    ASSIGN_LONG_G((short)1010, (byte)3, (byte)4), 
    ASSIGN_PET((short)1099, (byte)3, (byte)4);
    
    private short serial;
    private byte num;
    private byte length;
    
    public static byte getLength(final short serial, final byte index) {
        byte length = 0;
        for (final WarInstruct war : values()) {
            if (war.getSerial() == serial && index <= war.getNum()) {
                length = war.getLength();
            }
        }
        return length;
    }
    
    public short getSerial() {
        return this.serial;
    }
    
    public byte getNum() {
        return this.num;
    }
    
    public byte getLength() {
        return this.length;
    }
    
    private WarInstruct(final short serial, final byte num, final byte length) {
        this.serial = serial;
        this.num = num;
        this.length = length;
    }
}
