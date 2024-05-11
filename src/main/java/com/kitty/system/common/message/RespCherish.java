
package com.kitty.system.common.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6666)
public class RespCherish extends Message
{
    private int serial;
    private short num;
    
    public int getSerial() {
        return this.serial;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCherish)) {
            return false;
        }
        final RespCherish other = (RespCherish)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCherish;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCherish(serial=" + this.getSerial() + ", num=" + this.getNum() + ")";
    }
    
    public RespCherish() {
        this.serial = 0;
        this.num = 0;
    }
    
    @ConstructorProperties({ "serial", "num" })
    public RespCherish(final int serial, final short num) {
        this.serial = 0;
        this.num = 0;
        this.serial = serial;
        this.num = num;
    }
}
