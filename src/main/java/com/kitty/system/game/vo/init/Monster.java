
package com.kitty.system.game.vo.init;

import java.beans.ConstructorProperties;
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Monster)) {
            return false;
        }
        final Monster other = (Monster)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$pos = this.getPos();
        final Object other$pos = other.getPos();
        Label_0065: {
            if (this$pos == null) {
                if (other$pos == null) {
                    break Label_0065;
                }
            }
            else if (this$pos.equals(other$pos)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        if (this$serial == null) {
            if (other$serial == null) {
                return true;
            }
        }
        else if (this$serial.equals(other$serial)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Monster;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Monster(pos=" + this.getPos() + ", serial=" + this.getSerial() + ")";
    }
    
    public Monster() {
    }
    
    @ConstructorProperties({ "pos", "serial" })
    public Monster(final Integer pos, final Integer serial) {
        this.pos = pos;
        this.serial = serial;
    }
}
