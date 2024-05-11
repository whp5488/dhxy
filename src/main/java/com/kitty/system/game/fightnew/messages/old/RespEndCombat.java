
package com.kitty.system.game.fightnew.messages.old;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3581)
public class RespEndCombat extends Message
{
    private short flag;
    
    public short getFlag() {
        return this.flag;
    }
    
    public void setFlag(final short flag) {
        this.flag = flag;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespEndCombat)) {
            return false;
        }
        final RespEndCombat other = (RespEndCombat)o;
        return other.canEqual(this) && this.getFlag() == other.getFlag();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespEndCombat;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFlag();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespEndCombat(flag=" + this.getFlag() + ")";
    }
    
    @ConstructorProperties({ "flag" })
    public RespEndCombat(final short flag) {
        this.flag = 1;
        this.flag = flag;
    }
    
    public RespEndCombat() {
        this.flag = 1;
    }
}
