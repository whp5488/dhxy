package com.kitty.system.game.role.message.vo;

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
        Instruct other = (Instruct)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getIndex() != other.getIndex()) {
            return false;
        }
        Object this$str = this.getStr();
        Object other$str = other.getStr();
        return (this$str != null) ? this$str.equals(other$str) : (other$str == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Instruct;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getIndex();
        Object $str = this.getStr();
        result = result * 59 + (($str == null) ? 43 : $str.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Instruct(serial=" + (int)this.getSerial() + ", index=" + (int)this.getIndex() + ", str=" + this.getStr() + ")";
    }
    
    public Instruct(final short serial, final byte index, final String str) {
        this.serial = serial;
        this.index = index;
        this.str = str;
    }
    
    public Instruct() {
    }
}
