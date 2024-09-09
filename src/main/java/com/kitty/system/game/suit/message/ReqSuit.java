package com.kitty.system.game.suit.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4757)
public class ReqSuit extends Message
{
    public ReqSuit() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSuit)) {
            return false;
        }
        ReqSuit other = (ReqSuit)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSuit;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSuit()";
    }
}
