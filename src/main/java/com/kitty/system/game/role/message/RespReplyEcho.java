
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4275)
public class RespReplyEcho extends Message
{
    private String peerTime;
    
    public RespReplyEcho() {
        this.peerTime = String.valueOf(System.currentTimeMillis());
    }
    
    public String getPeerTime() {
        return this.peerTime;
    }
    
    public void setPeerTime(final String peerTime) {
        this.peerTime = peerTime;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespReplyEcho)) {
            return false;
        }
        final RespReplyEcho other = (RespReplyEcho)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$peerTime = this.getPeerTime();
        final Object other$peerTime = other.getPeerTime();
        if (this$peerTime == null) {
            if (other$peerTime == null) {
                return true;
            }
        }
        else if (this$peerTime.equals(other$peerTime)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespReplyEcho;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $peerTime = this.getPeerTime();
        result = result * 59 + (($peerTime == null) ? 43 : $peerTime.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespReplyEcho(peerTime=" + this.getPeerTime() + ")";
    }
}
