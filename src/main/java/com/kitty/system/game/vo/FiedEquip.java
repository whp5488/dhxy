
package com.kitty.system.game.vo;

public class FiedEquip
{
    private Integer type;
    private Integer value;
    private double rate;
    
    public FiedEquip() {
        this.rate = 0.0;
    }
    
    public FiedEquip(final Integer type, final Integer value) {
        this.rate = 0.0;
        this.type = type;
        this.value = value;
    }
    
    public FiedEquip(final Integer type, final Integer value, final double rate) {
        this.rate = 0.0;
        this.type = type;
        this.value = value;
        this.rate = rate;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    public double getRate() {
        return this.rate;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setValue(final Integer value) {
        this.value = value;
    }
    
    public void setRate(final double rate) {
        this.rate = rate;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FiedEquip)) {
            return false;
        }
        final FiedEquip other = (FiedEquip)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return Double.compare(this.getRate(), other.getRate()) == 0;
            }
        }
        else if (this$value.equals(other$value)) {
            return Double.compare(this.getRate(), other.getRate()) == 0;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FiedEquip;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        final long $rate = Double.doubleToLongBits(this.getRate());
        result = result * 59 + (int)($rate >>> 32 ^ $rate);
        return result;
    }
    
    @Override
    public String toString() {
        return "FiedEquip(type=" + this.getType() + ", value=" + this.getValue() + ", rate=" + this.getRate() + ")";
    }
}
