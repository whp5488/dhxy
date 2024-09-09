package com.kitty.system.game.vo;

public class FieldProp
{
    private Integer type;
    private Integer value;
    
    public FieldProp() {
    }
    
    public FieldProp(Integer type, Integer value) {
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
}
