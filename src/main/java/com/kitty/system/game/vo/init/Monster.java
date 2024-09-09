package com.kitty.system.game.vo.init;

import java.io.Serializable;

public class Monster implements Serializable
{
    private Integer pos;
    private Integer serial;
    
    public Integer getPos() {
        return this.pos;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public void setPos(final Integer pos) {
        this.pos = pos;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public Monster() {
    }
    
    public Monster(final Integer pos, final Integer serial) {
        this.pos = pos;
        this.serial = serial;
    }
}
