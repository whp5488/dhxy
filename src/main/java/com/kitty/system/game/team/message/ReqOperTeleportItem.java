
package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16590)
public class ReqOperTeleportItem extends Message
{
    private int id;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqOperTeleportItem)) {
            return false;
        }
        final ReqOperTeleportItem other = (ReqOperTeleportItem)o;
        return other.canEqual(this) && this.getId() == other.getId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqOperTeleportItem;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqOperTeleportItem(id=" + this.getId() + ")";
    }
}
