package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49000)
public class RespActivityOpenNotice extends Message
{
    private short type;
    
    public RespActivityOpenNotice() {
        this.type = 1010;
    }
    
    public short getType() {
        return this.type;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespActivityOpenNotice)) {
            return false;
        }
        RespActivityOpenNotice other = (RespActivityOpenNotice)o;
        return other.canEqual(this) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespActivityOpenNotice;
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
        return "RespActivityOpenNotice(type=" + (int)this.getType() + ")";
    }
}
