package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4308)
public class RespPotholingNotice extends Message
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
        if (!(o instanceof RespPotholingNotice)) {
            return false;
        }
        RespPotholingNotice other = (RespPotholingNotice)o;
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
        return other instanceof RespPotholingNotice;
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
        return "RespPotholingNotice(serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
    
    public RespPotholingNotice(final Integer serial, final Integer num) {
        this.serial = serial;
        this.num = num;
    }
}
