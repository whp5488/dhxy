package com.kitty.system.game.welfare.message.vo;

public class LuckDrawInfo
{
    private int serial;
    private int num;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }

    public LuckDrawInfo() {
    }
    
    public LuckDrawInfo(final int serial, final int num) {
        this.serial = serial;
        this.num = num;
    }
}
