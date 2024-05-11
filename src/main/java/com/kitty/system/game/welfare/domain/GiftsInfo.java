
package com.kitty.system.game.welfare.domain;

import java.beans.ConstructorProperties;

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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GiftsInfo)) {
            return false;
        }
        final GiftsInfo other = (GiftsInfo)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GiftsInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "GiftsInfo(serial=" + this.getSerial() + ", state=" + this.getState() + ")";
    }
    
    @ConstructorProperties({ "serial", "state" })
    public GiftsInfo(final short serial, final byte state) {
        this.serial = serial;
        this.state = state;
    }
    
    public GiftsInfo() {
    }
}
