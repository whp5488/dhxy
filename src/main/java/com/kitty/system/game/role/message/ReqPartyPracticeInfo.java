
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28841)
public class ReqPartyPracticeInfo extends Message
{
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyPracticeInfo)) {
            return false;
        }
        final ReqPartyPracticeInfo other = (ReqPartyPracticeInfo)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyPracticeInfo;
    }
    
    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyPracticeInfo()";
    }
}
