
package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45231)
public class ReqReleaseInfo extends Message
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
        if (!(o instanceof ReqReleaseInfo)) {
            return false;
        }
        final ReqReleaseInfo other = (ReqReleaseInfo)o;
        return other.canEqual(this) && this.getId() == other.getId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqReleaseInfo;
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
        return "ReqReleaseInfo(id=" + this.getId() + ")";
    }
}
