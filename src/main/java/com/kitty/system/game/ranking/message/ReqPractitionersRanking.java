package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4224)
public class ReqPractitionersRanking extends Message
{
    public ReqPractitionersRanking() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPractitionersRanking)) {
            return false;
        }
        ReqPractitionersRanking other = (ReqPractitionersRanking)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPractitionersRanking;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPractitionersRanking()";
    }
}
