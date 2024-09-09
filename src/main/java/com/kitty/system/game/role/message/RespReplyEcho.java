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
        RespReplyEcho other = (RespReplyEcho)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$peerTime = this.getPeerTime();
        Object other$peerTime = other.getPeerTime();
        return (this$peerTime != null) ? this$peerTime.equals(other$peerTime) : (other$peerTime == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespReplyEcho;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $peerTime = this.getPeerTime();
        result = result * 59 + (($peerTime == null) ? 43 : $peerTime.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespReplyEcho(peerTime=" + this.getPeerTime() + ")";
    }
}
