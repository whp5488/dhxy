
package com.kitty.system.game.vo;

public class FieldProp
{
    private Integer type;
    private Integer value;
    
    public FieldProp() {
    }
    
    public FieldProp(final Integer type, final Integer value) {
        this.type = type;
        this.value = value;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setValue(final Integer value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FieldProp)) {
            return false;
        }
        final FieldProp other = (FieldProp)o;
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
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof FieldProp;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "FieldProp(type=" + this.getType() + ", value=" + this.getValue() + ")";
    }
}
