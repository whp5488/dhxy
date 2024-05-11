
package com.kitty.system.game.vo;

import java.io.Serializable;

public class FieldByte implements Serializable
{
    private Integer k;
    private Byte v;
    
    public FieldByte() {
    }
    
    public FieldByte(final Integer k, final Byte v) {
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldByte)) {
            return false;
        }
        final FieldByte other = (FieldByte)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$k = this.getK();
        final Object other$k = other.getK();
        Label_0065: {
            if (this$k == null) {
                if (other$k == null) {
                    break Label_0065;
                }
            }
            else if (this$k.equals(other$k)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$v = this.getV();
        final Object other$v = other.getV();
        if (this$v == null) {
            if (other$v == null) {
                return true;
            }
        }
        else if (this$v.equals(other$v)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FieldByte;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $k = this.getK();
        result = result * 59 + (($k == null) ? 43 : $k.hashCode());
        final Object $v = this.getV();
        result = result * 59 + (($v == null) ? 43 : $v.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FieldByte(k=" + this.getK() + ", v=" + this.getV() + ")";
    }
}
