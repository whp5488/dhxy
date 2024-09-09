package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45061)
public class ReqChange extends Message
{
    private int serial;
    private byte type;
    
    public ReqChange() {
        this.serial = 0;
        this.type = 0;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqChange)) {
            return false;
        }
        ReqChange other = (ReqChange)o;
        return other.canEqual(this) && this.getSerial() == other.getSerial() && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqChange;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChange(serial=" + this.getSerial() + ", type=" + (int)this.getType() + ")";
    }
}
