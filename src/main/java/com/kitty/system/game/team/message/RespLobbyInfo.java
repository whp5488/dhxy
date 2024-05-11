
package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45075)
public class RespLobbyInfo extends Message
{
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespLobbyInfo)) {
            return false;
        }
        final RespLobbyInfo other = (RespLobbyInfo)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespLobbyInfo;
    }
    
    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "RespLobbyInfo()";
    }
}
