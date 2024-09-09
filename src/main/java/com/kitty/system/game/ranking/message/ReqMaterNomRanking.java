package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4227)
public class ReqMaterNomRanking extends Message
{
    public ReqMaterNomRanking() {
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqMaterNomRanking)) {
            return false;
        }
        ReqMaterNomRanking other = (ReqMaterNomRanking)o;
        return other.canEqual(this);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMaterNomRanking;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMaterNomRanking()";
    }
}
