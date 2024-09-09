package com.kitty.system.game.equip.message.vo;

import java.io.Serializable;

public class EffectInfo implements Serializable
{
    private int serial;
    private byte state;
    private int time;
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EffectInfo)) {
            return false;
        }
        EffectInfo other = (EffectInfo)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getState() == other.getState() && this.getTime() == other.getTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof EffectInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getState();
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "EffectInfo(serial=" + this.getSerial() + ", state=" + (int)this.getState() + ", time=" + this.getTime() + ")";
    }
    
    public EffectInfo(final int serial, final byte state, final int time) {
        this.serial = serial;
        this.state = state;
        this.time = time;
    }
    
    public EffectInfo() {
    }
}
