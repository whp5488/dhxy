package com.kitty.system.game.bag.message.vo;

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
        PotholingVo other = (PotholingVo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$num = this.getNum();
        Object other$num = other.getNum();
        return (this$num != null) ? this$num.equals(other$num) : (other$num == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PotholingVo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PotholingVo(serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
    
    public PotholingVo(final Integer serial, final Integer num) {
        this.serial = serial;
        this.num = num;
    }
    
    public PotholingVo() {
    }
}
