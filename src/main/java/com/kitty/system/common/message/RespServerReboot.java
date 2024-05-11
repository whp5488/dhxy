
package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45565)
public class RespServerReboot extends Message
{
    String tips;
    
    public RespServerReboot() {
        this.tips = "由于游戏服务器维护，您的角色已断开连接。";
    }
    
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
        if (!(o instanceof RespServerReboot)) {
            return false;
        }
        final RespServerReboot other = (RespServerReboot)o;
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
        return other instanceof RespServerReboot;
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
        return "RespServerReboot(tips=" + this.getTips() + ")";
    }
}
