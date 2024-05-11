
package com.kitty.system.game.enter.message;

import java.beans.ConstructorProperties;
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
        final RespKickClient other = (RespKickClient)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$tips = this.getTips();
        final Object other$tips = other.getTips();
        if (this$tips == null) {
            if (other$tips == null) {
                return true;
            }
        }
        else if (this$tips.equals(other$tips)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespKickClient;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespKickClient(tips=" + this.getTips() + ")";
    }
    
    @ConstructorProperties({ "tips" })
    public RespKickClient(final String tips) {
        this.tips = tips;
    }
    
    public RespKickClient() {
    }
}
