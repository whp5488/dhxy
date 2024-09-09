package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 518)
public class ReqRecover extends Message
{
    private byte type;
    
    public ReqRecover() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqRecover)) {
            return false;
        }
        ReqRecover other = (ReqRecover)o;
        return other.canEqual(this) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqRecover;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqRecover(type=" + (int)this.getType() + ")";
    }
}
