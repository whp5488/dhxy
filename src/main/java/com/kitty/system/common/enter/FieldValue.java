
package com.kitty.system.common.enter;

import java.beans.ConstructorProperties;
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldValue)) {
            return false;
        }
        final FieldValue other = (FieldValue)o;
        return other.canEqual(this) && this.getK() == other.getK() && this.getV() == other.getV();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FieldValue;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getK();
        result = result * 59 + this.getV();
        return result;
    }
    
    @Override
    public String toString() {
        return "FieldValue(k=" + this.getK() + ", v=" + this.getV() + ")";
    }
    
    public FieldValue() {
    }
    
    @ConstructorProperties({ "k", "v" })
    public FieldValue(final int k, final int v) {
        this.k = k;
        this.v = v;
    }
}
