
package com.kitty.system.game.bag.message.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class PotholingVo implements Serializable
{
    private Integer serial;
    private Integer num;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PotholingVo)) {
            return false;
        }
        final PotholingVo other = (PotholingVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$num = this.getNum();
        final Object other$num = other.getNum();
        if (this$num == null) {
            if (other$num == null) {
                return true;
            }
        }
        else if (this$num.equals(other$num)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PotholingVo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PotholingVo(serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
    
    @ConstructorProperties({ "serial", "num" })
    public PotholingVo(final Integer serial, final Integer num) {
        this.serial = serial;
        this.num = num;
    }
    
    public PotholingVo() {
    }
}
