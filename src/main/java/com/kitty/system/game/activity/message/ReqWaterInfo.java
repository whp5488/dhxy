package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49161)
public class ReqWaterInfo extends Message
{
    public ReqWaterInfo() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqWaterInfo)) {
            return false;
        }
        ReqWaterInfo other = (ReqWaterInfo)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqWaterInfo;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqWaterInfo()";
    }
}
