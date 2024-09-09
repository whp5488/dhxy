package com.kitty.system.game.plug.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45304)
public class RespPlugExecuteInfo extends Message
{
    private int serial;
    private int num;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPlugExecuteInfo)) {
            return false;
        }
        RespPlugExecuteInfo other = (RespPlugExecuteInfo)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPlugExecuteInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPlugExecuteInfo(serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
    
    public RespPlugExecuteInfo(final int serial, final int num) {
        this.serial = serial;
        this.num = num;
    }
    
    public RespPlugExecuteInfo() {
    }
}
