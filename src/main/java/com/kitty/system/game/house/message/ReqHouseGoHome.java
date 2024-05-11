
package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20586)
public class ReqHouseGoHome extends Message
{
    private byte type;
    
    public ReqHouseGoHome() {
        this.type = 0;
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
        if (!(o instanceof ReqHouseGoHome)) {
            return false;
        }
        final ReqHouseGoHome other = (ReqHouseGoHome)o;
        return other.canEqual(this) && this.getType() == other.getType();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqHouseGoHome;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqHouseGoHome(type=" + this.getType() + ")";
    }
}
