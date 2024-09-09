package com.kitty.system.game.vo;

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

    public UnifiedValue() {
    }
    
    public UnifiedValue(final String type, final int value) {
        this.type = type;
        this.value = value;
    }
}
