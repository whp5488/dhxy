
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28844)
public class ReqResetAssignPartyPractice extends Message
{
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqResetAssignPartyPractice)) {
            return false;
        }
        final ReqResetAssignPartyPractice other = (ReqResetAssignPartyPractice)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqResetAssignPartyPractice;
    }
    
    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqResetAssignPartyPractice()";
    }
}
