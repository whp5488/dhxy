
package com.kitty.system.game.vo;

import java.beans.ConstructorProperties;

public class UnifiedValue
{
    private String type;
    private int value;
    
    public String getType() {
        return this.type;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof UnifiedValue)) {
            return false;
        }
        final UnifiedValue other = (UnifiedValue)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null) {
            if (other$type == null) {
                return this.getValue() == other.getValue();
            }
        }
        else if (this$type.equals(other$type)) {
            return this.getValue() == other.getValue();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof UnifiedValue;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        result = result * 59 + this.getValue();
        return result;
    }
    
    @Override
    public String toString() {
        return "UnifiedValue(type=" + this.getType() + ", value=" + this.getValue() + ")";
    }
    
    public UnifiedValue() {
    }
    
    @ConstructorProperties({ "type", "value" })
    public UnifiedValue(final String type, final int value) {
        this.type = type;
        this.value = value;
    }
}
