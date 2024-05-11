
package com.kitty.system.game.role.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Instruct implements Serializable
{
    private short serial;
    private byte index;
    private String str;
    
    public short getSerial() {
        return this.serial;
    }
    
    public byte getIndex() {
        return this.index;
    }
    
    public String getStr() {
        return this.str;
    }
    
    public void setSerial(final short serial) {
        this.serial = serial;
    }
    
    public void setIndex(final byte index) {
        this.index = index;
    }
    
    public void setStr(final String str) {
        this.str = str;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Instruct)) {
            return false;
        }
        final Instruct other = (Instruct)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getIndex() != other.getIndex()) {
            return false;
        }
        final Object this$str = this.getStr();
        final Object other$str = other.getStr();
        if (this$str == null) {
            if (other$str == null) {
                return true;
            }
        }
        else if (this$str.equals(other$str)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Instruct;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getIndex();
        final Object $str = this.getStr();
        result = result * 59 + (($str == null) ? 43 : $str.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Instruct(serial=" + this.getSerial() + ", index=" + this.getIndex() + ", str=" + this.getStr() + ")";
    }
    
    @ConstructorProperties({ "serial", "index", "str" })
    public Instruct(final short serial, final byte index, final String str) {
        this.serial = serial;
        this.index = index;
        this.str = str;
    }
    
    public Instruct() {
    }
}
