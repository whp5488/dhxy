package com.kitty.system.init.domain.vo;

import java.io.Serializable;

public class MonsterPower implements Serializable
{
    private double a;
    private double b;
    private double c;
    
    public double getA() {
        return this.a;
    }
    
    public double getB() {
        return this.b;
    }
    
    public double getC() {
        return this.c;
    }
    
    public void setA(final double a) {
        this.a = a;
    }
    
    public void setB(final double b) {
        this.b = b;
    }
    
    public void setC(final double c) {
        this.c = c;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MonsterPower)) {
            return false;
        }
        MonsterPower other = (MonsterPower)o;
        return other.canEqual(this) && Double.compare(this.getA(), other.getA()) == 0 && Double.compare(this.getB(), other.getB()) == 0 && Double.compare(this.getC(), other.getC()) == 0;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MonsterPower;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $a = Double.doubleToLongBits(this.getA());
        result = result * 59 + (int)($a >>> 32 ^ $a);
        long $b = Double.doubleToLongBits(this.getB());
        result = result * 59 + (int)($b >>> 32 ^ $b);
        long $c = Double.doubleToLongBits(this.getC());
        result = result * 59 + (int)($c >>> 32 ^ $c);
        return result;
    }
    
    @Override
    public String toString() {
        return "MonsterPower(a=" + this.getA() + ", b=" + this.getB() + ", c=" + this.getC() + ")";
    }
    
    public MonsterPower(final double a, final double b, final double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
    
    public MonsterPower() {
    }
}
