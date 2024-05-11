
package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40971)
public class RespGiftState extends Message
{
    private short type;
    private byte state;
    private byte is;
    
    public RespGiftState() {
        this.type = 1;
        this.state = 1;
        this.is = 1;
    }
    
    public short getType() {
        return this.type;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public byte getIs() {
        return this.is;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setIs(final byte is) {
        this.is = is;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGiftState)) {
            return false;
        }
        final RespGiftState other = (RespGiftState)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getState() == other.getState() && this.getIs() == other.getIs();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespGiftState;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getState();
        result = result * 59 + this.getIs();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGiftState(type=" + this.getType() + ", state=" + this.getState() + ", is=" + this.getIs() + ")";
    }
}
