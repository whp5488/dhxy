package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6890)
public class ReqMotto extends Message
{
    private String motto;
    
    public ReqMotto() {
    }
    
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
        ReqMotto other = (ReqMotto)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$motto = this.getMotto();
        Object other$motto = other.getMotto();
        return (this$motto != null) ? this$motto.equals(other$motto) : (other$motto == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMotto;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $motto = this.getMotto();
        result = result * 59 + (($motto == null) ? 43 : $motto.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMotto(motto=" + this.getMotto() + ")";
    }
}
