
package com.kitty.system.game.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Field implements Serializable
{
    private Integer pos;
    private Integer min;
    private Integer max;
    
    public Integer getPos() {
        return this.pos;
    }
    
    public Integer getMin() {
        return this.min;
    }
    
    public Integer getMax() {
        return this.max;
    }
    
    public void setPos(final Integer pos) {
        this.pos = pos;
    }
    
    public void setMin(final Integer min) {
        this.min = min;
    }
    
    public void setMax(final Integer max) {
        this.max = max;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Field)) {
            return false;
        }
        final Field other = (Field)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$pos = this.getPos();
        final Object other$pos = other.getPos();
        Label_0065: {
            if (this$pos == null) {
                if (other$pos == null) {
                    break Label_0065;
                }
            }
            else if (this$pos.equals(other$pos)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$min = this.getMin();
        final Object other$min = other.getMin();
        Label_0102: {
            if (this$min == null) {
                if (other$min == null) {
                    break Label_0102;
                }
            }
            else if (this$min.equals(other$min)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$max = this.getMax();
        final Object other$max = other.getMax();
        if (this$max == null) {
            if (other$max == null) {
                return true;
            }
        }
        else if (this$max.equals(other$max)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Field;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        final Object $min = this.getMin();
        result = result * 59 + (($min == null) ? 43 : $min.hashCode());
        final Object $max = this.getMax();
        result = result * 59 + (($max == null) ? 43 : $max.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Field(pos=" + this.getPos() + ", min=" + this.getMin() + ", max=" + this.getMax() + ")";
    }
    
    public Field() {
    }
    
    @ConstructorProperties({ "pos", "min", "max" })
    public Field(final Integer pos, final Integer min, final Integer max) {
        this.pos = pos;
        this.min = min;
        this.max = max;
    }
}
