
package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41482)
public class RespPrizeDraw extends Message
{
    private short count;
    
    public RespPrizeDraw() {
        this.count = 0;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPrizeDraw)) {
            return false;
        }
        final RespPrizeDraw other = (RespPrizeDraw)o;
        return other.canEqual(this) && this.getCount() == other.getCount();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPrizeDraw;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCount();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPrizeDraw(count=" + this.getCount() + ")";
    }
}
