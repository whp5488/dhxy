package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45229)
public class ReqTraceGo extends Message
{
    public ReqTraceGo() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqTraceGo)) {
            return false;
        }
        ReqTraceGo other = (ReqTraceGo)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqTraceGo;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqTraceGo()";
    }
}
