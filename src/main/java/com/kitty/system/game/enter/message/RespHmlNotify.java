
package com.kitty.system.game.enter.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45388)
public class RespHmlNotify extends Message
{
    private short serial;
    private byte state;
    
    public short getSerial() {
        return this.serial;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setSerial(final short serial) {
        this.serial = serial;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespHmlNotify)) {
            return false;
        }
        final RespHmlNotify other = (RespHmlNotify)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getState() == other.getState();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespHmlNotify;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getState();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespHmlNotify(serial=" + this.getSerial() + ", state=" + this.getState() + ")";
    }
    
    @ConstructorProperties({ "serial", "state" })
    public RespHmlNotify(final short serial, final byte state) {
        this.serial = serial;
        this.state = state;
    }
    
    public RespHmlNotify() {
    }
}
