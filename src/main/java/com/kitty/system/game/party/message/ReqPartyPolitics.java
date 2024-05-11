
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20534)
public class ReqPartyPolitics extends Message
{
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyPolitics)) {
            return false;
        }
        final ReqPartyPolitics other = (ReqPartyPolitics)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyPolitics;
    }
    
    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyPolitics()";
    }
}
