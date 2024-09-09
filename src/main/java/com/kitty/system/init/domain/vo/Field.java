package com.kitty.system.init.domain.vo;

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
        Field other = (Field)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$key = this.getKey();
        Object other$key = other.getKey();
        if (!((this$key != null) ? this$key.equals(other$key) : (other$key == null))) {
            return false;
        }
        Object this$value = this.getValue();
        Object other$value = other.getValue();
        return (this$value != null) ? this$value.equals(other$value) : (other$value == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Field;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $key = this.getKey();
        result = result * 59 + (($key == null) ? 43 : $key.hashCode());
        Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Field(key=" + this.getKey() + ", value=" + this.getValue() + ")";
    }
    
    public Field() {
    }
    
    public Field(final Integer key, final Double value) {
        this.key = key;
        this.value = value;
    }
}
