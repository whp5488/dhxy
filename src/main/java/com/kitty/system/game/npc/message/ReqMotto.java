
package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6890)
public class ReqMotto extends Message
{
    private String motto;
    
    public String getMotto() {
        return this.motto;
    }
    
    public void setMotto(final String motto) {
        this.motto = motto;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqMotto)) {
            return false;
        }
        final ReqMotto other = (ReqMotto)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$motto = this.getMotto();
        final Object other$motto = other.getMotto();
        if (this$motto == null) {
            if (other$motto == null) {
                return true;
            }
        }
        else if (this$motto.equals(other$motto)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMotto;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $motto = this.getMotto();
        result = result * 59 + (($motto == null) ? 43 : $motto.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMotto(motto=" + this.getMotto() + ")";
    }
}
