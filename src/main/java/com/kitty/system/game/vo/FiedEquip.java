package com.kitty.system.game.vo;

public class FiedEquip
{
    private Integer type;
    private Integer value;
    private double rate;
    
    public FiedEquip() {
        this.rate = 0.0;
    }
    
    public FiedEquip(Integer type, Integer value) {
        this.rate = 0.0;
        this.type = type;
        this.value = value;
    }
    
    public FiedEquip(Integer type, Integer value, double rate) {
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
}
