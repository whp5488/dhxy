package com.kitty.system.game.fightRecord.domain;

import java.io.Serializable;

public class SkillRecord implements Serializable
{
    private int o;
    private int r;
    private int p;
    private long a;
    private long t;
    private int s;
    
    public int getO() {
        return this.o;
    }
    
    public int getR() {
        return this.r;
    }
    
    public int getP() {
        return this.p;
    }
    
    public long getA() {
        return this.a;
    }
    
    public long getT() {
        return this.t;
    }
    
    public int getS() {
        return this.s;
    }
    
    public void setO(final int o) {
        this.o = o;
    }
    
    public void setR(final int r) {
        this.r = r;
    }
    
    public void setP(final int p) {
        this.p = p;
    }
    
    public void setA(final long a) {
        this.a = a;
    }
    
    public void setT(final long t) {
        this.t = t;
    }
    
    public void setS(final int s) {
        this.s = s;
    }
    
    public SkillRecord() {
    }
    
    public SkillRecord(final int o, final int r, final int p, final long a, final long t, final int s) {
        this.o = o;
        this.r = r;
        this.p = p;
        this.a = a;
        this.t = t;
        this.s = s;
    }
}
