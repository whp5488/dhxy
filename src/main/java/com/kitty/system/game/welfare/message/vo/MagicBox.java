
package com.kitty.system.game.welfare.message.vo;

import java.beans.ConstructorProperties;
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MagicBox)) {
            return false;
        }
        final MagicBox other = (MagicBox)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getNum() == other.getNum() && this.getBd() == other.getBd();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MagicBox;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        result = result * 59 + this.getBd();
        return result;
    }
    
    @Override
    public String toString() {
        return "MagicBox(serial=" + this.getSerial() + ", num=" + this.getNum() + ", bd=" + this.getBd() + ")";
    }
    
    @ConstructorProperties({ "serial", "num", "bd" })
    public MagicBox(final int serial, final short num, final int bd) {
        this.serial = serial;
        this.num = num;
        this.bd = bd;
    }
    
    public MagicBox() {
    }
}
