package com.kitty.system.game.vo;

import java.io.Serializable;

public class FieldByte implements Serializable
{
    private Integer k;
    private Byte v;
    
    public FieldByte() {
    }
    
    public FieldByte(Integer k, Byte v) {
        this.k = k;
        this.v = v;
    }
    
    public Integer getK() {
        return this.k;
    }
    
    public Byte getV() {
        return this.v;
    }
    
    public void setK(final Integer k) {
        this.k = k;
    }
    
    public void setV(final Byte v) {
        this.v = v;
    }
}
