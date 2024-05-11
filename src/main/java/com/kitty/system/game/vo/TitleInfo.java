
package com.kitty.system.game.vo;

import java.io.Serializable;

public class TitleInfo implements Serializable
{
    private int serial;
    private int time;
    
    public TitleInfo(final int serial, final int time) {
        this.serial = serial;
        this.time = time;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof TitleInfo)) {
            return false;
        }
        final TitleInfo other = (TitleInfo)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getTime() == other.getTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof TitleInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "TitleInfo(serial=" + this.getSerial() + ", time=" + this.getTime() + ")";
    }
    
    public TitleInfo() {
    }
}
