package com.kitty.system.game.welfare.message.vo;

import java.io.Serializable;

public class MagicBox implements Serializable
{
    private int serial;
    private short num;
    private int bd;
    
    public int getSerial() {
        return this.serial;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public MagicBox(final int serial, final short num, final int bd) {
        this.serial = serial;
        this.num = num;
        this.bd = bd;
    }
    
    public MagicBox() {
    }
}
