
package com.kitty.system.init.domain.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Field implements Serializable
{
    private Integer key;
    private Double value;
    
    public Integer getKey() {
        return this.key;
    }
    
    public Double getValue() {
        return this.value;
    }
    
    public void setKey(final Integer key) {
        this.key = key;
    }
    
    public void setValue(final Double value) {
        this.value = value;
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
        final Object this$key = this.getKey();
        final Object other$key = other.getKey();
        Label_0065: {
            if (this$key == null) {
                if (other$key == null) {
                    break Label_0065;
                }
            }
            else if (this$key.equals(other$key)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
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
        final Object $key = this.getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Field(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }
    
    public Field() {
    }
    
    @ConstructorProperties({ "key", "value" })
    public Field(final Integer key, final Double value) {
        this.key = key;
        this.value = value;
    }
}
