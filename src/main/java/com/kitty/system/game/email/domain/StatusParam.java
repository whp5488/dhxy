package com.kitty.system.game.email.domain;

public class StatusParam
{
    private long key;
    private byte value;
    
    public long getKey() {
        return this.key;
    }
    
    public byte getValue() {
        return this.value;
    }
    
    public void setKey(final long key) {
        this.key = key;
    }
    
    public void setValue(final byte value) {
        this.value = value;
    }
    
    public StatusParam() {
    }
    
    public StatusParam(final long key, final byte value) {
        this.key = key;
        this.value = value;
    }
}
