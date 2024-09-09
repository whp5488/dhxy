package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53569)
public class RespTempFriendState extends Message
{
    private String gid;
    private int online;
    
    public RespTempFriendState() {
    }
    
    public String getGid() {
        return this.gid;
    }
    
    public int getOnline() {
        return this.online;
    }
    
    public void setGid(final String gid) {
        this.gid = gid;
    }
    
    public void setOnline(final int online) {
        this.online = online;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTempFriendState)) {
            return false;
        }
        RespTempFriendState other = (RespTempFriendState)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$gid = this.getGid();
        Object other$gid = other.getGid();
        return ((this$gid != null) ? this$gid.equals(other$gid) : (other$gid == null)) && this.getOnline() == other.getOnline();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTempFriendState;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $gid = this.getGid();
        result = result * 59 + (($gid == null) ? 43 : $gid.hashCode());
        result = result * 59 + this.getOnline();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTempFriendState(gid=" + this.getGid() + ", online=" + this.getOnline() + ")";
    }
}
