package com.kitty.system.game.vo;

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

    public Field() {
    }
    
    public Field(final Integer pos, final Integer min, final Integer max) {
        this.pos = pos;
        this.min = min;
        this.max = max;
    }
}
