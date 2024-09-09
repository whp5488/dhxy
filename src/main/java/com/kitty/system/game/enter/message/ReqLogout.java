package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4)
public class ReqLogout extends Message
{
    private Integer reason;
    
    public ReqLogout() {
    }
    
    public Integer getReason() {
        return this.reason;
    }
    
    public void setReason(final Integer reason) {
        this.reason = reason;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqLogout)) {
            return false;
        }
        ReqLogout other = (ReqLogout)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$reason = this.getReason();
        Object other$reason = other.getReason();
        return (this$reason != null) ? this$reason.equals(other$reason) : (other$reason == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqLogout;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $reason = this.getReason();
        result = result * 59 + (($reason == null) ? 43 : $reason.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqLogout(reason=" + this.getReason() + ")";
    }
}
