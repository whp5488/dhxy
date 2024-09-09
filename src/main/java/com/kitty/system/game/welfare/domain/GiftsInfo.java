package com.kitty.system.game.welfare.domain;

public class GiftsInfo
{
    private short serial;
    private byte state;
    
    public short getSerial() {
        return this.serial;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setSerial(final short serial) {
        this.serial = serial;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public GiftsInfo(final short serial, final byte state) {
        this.serial = serial;
        this.state = state;
    }
    
    public GiftsInfo() {
    }
}
