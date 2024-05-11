
package com.kitty.system.init.domain.vo;

public class Prop
{
    private int serial;
    private int onset;
    private int over;
    private double prop;
    
    public Prop() {
        this.serial = 0;
        this.onset = 0;
        this.over = 0;
        this.prop = 0.0;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getOnset() {
        return this.onset;
    }
    
    public int getOver() {
        return this.over;
    }
    
    public double getProp() {
        return this.prop;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setOnset(final int onset) {
        this.onset = onset;
    }
    
    public void setOver(final int over) {
        this.over = over;
    }
    
    public void setProp(final double prop) {
        this.prop = prop;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Prop)) {
            return false;
        }
        final Prop other = (Prop)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getOnset() == other.getOnset() && this.getOver() == other.getOver() && Double.compare(this.getProp(), other.getProp()) == 0;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Prop;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getOnset();
        result = result * 59 + this.getOver();
        final long $prop = Double.doubleToLongBits(this.getProp());
        result = result * 59 + (int)($prop >>> 32 ^ $prop);
        return result;
    }
    
    @Override
    public String toString() {
        return "Prop(serial=" + this.getSerial() + ", onset=" + this.getOnset() + ", over=" + this.getOver() + ", prop=" + this.getProp() + ")";
    }
}
