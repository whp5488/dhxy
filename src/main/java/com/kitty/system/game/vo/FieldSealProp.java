package com.kitty.system.game.vo;

import com.kitty.system.game.train.domain.vo.FieldSeal;
import java.io.Serializable;

public class FieldSealProp implements Serializable
{
    private int t;
    private int k;
    private int v;
    
    public FieldSealProp() {
    }
    
    public FieldSealProp(int t, int k, int v) {
        this.t = t;
        this.k = k;
        this.v = v;
    }
    
    public FieldSealProp(FieldSeal seal) {
        this.t = seal.getT();
        this.k = seal.getK();
        this.v = seal.getV();
    }
    
    public int getT() {
        return this.t;
    }
    
    public int getK() {
        return this.k;
    }
    
    public int getV() {
        return this.v;
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
}
