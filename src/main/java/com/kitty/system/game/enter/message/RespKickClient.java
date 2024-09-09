package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53405)
public class RespKickClient extends Message
{
    private String tips;
    
    public String getTips() {
        return this.tips;
    }
    
    public void setTips(final String tips) {
        this.tips = tips;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespKickClient)) {
            return false;
        }
        RespKickClient other = (RespKickClient)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$tips = this.getTips();
        Object other$tips = other.getTips();
        return (this$tips != null) ? this$tips.equals(other$tips) : (other$tips == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespKickClient;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespKickClient(tips=" + this.getTips() + ")";
    }
    
    public RespKickClient(final String tips) {
        this.tips = tips;
    }
    
    public RespKickClient() {
    }
}
