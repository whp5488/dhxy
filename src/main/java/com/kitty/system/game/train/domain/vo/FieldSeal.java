package com.kitty.system.game.train.domain.vo;

import java.io.Serializable;

public class FieldSeal implements Serializable
{
    private int t;
    private int k;
    private int v;
    private double r;
    
    public int getT() {
        return this.t;
    }
    
    public int getK() {
        return this.k;
    }
    
    public int getV() {
        return this.v;
    }
    
    public double getR() {
        return this.r;
    }
    
    public void setT(final int t) {
        this.t = t;
    }
    
    public void setK(final int k) {
        this.k = k;
    }
    
    public void setV(final int v) {
        this.v = v;
    }
    
    public void setR(final double r) {
        this.r = r;
    }
    
    public FieldSeal(final int t, final int k, final int v, final double r) {
        this.r = 0.0;
        this.t = t;
        this.k = k;
        this.v = v;
        this.r = r;
    }
    
    public FieldSeal() {
        this.r = 0.0;
    }
}
