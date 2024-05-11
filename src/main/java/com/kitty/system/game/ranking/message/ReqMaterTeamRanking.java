
package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4228)
public class ReqMaterTeamRanking extends Message
{
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqMaterTeamRanking)) {
            return false;
        }
        final ReqMaterTeamRanking other = (ReqMaterTeamRanking)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMaterTeamRanking;
    }
    
    @Override
    public int hashCode() {
        final int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMaterTeamRanking()";
    }
}
