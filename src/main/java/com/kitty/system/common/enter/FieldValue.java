package com.kitty.system.common.enter;

import java.io.Serializable;

public class FieldValue implements Serializable
{
    private int k;
    private int v;
    
    public int getK() {
        return this.k;
    }
    
    public int getV() {
        return this.v;
    }
    
    public void setK(final int k) {
        this.k = k;
    }
    
    public void setV(final int v) {
        this.v = v;
    }
    
    public FieldValue() {
    }
    
    public FieldValue(final int k, final int v) {
        this.k = k;
        this.v = v;
    }
}
