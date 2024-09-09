package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4223)
public class ReqComprehensiveStrengthRanking extends Message
{
    private byte race;
    private byte serverId;
    
    public ReqComprehensiveStrengthRanking() {
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqComprehensiveStrengthRanking)) {
            return false;
        }
        ReqComprehensiveStrengthRanking other = (ReqComprehensiveStrengthRanking)o;
        return other.canEqual(this) && this.getRace() == other.getRace() && this.getServerId() == other.getServerId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqComprehensiveStrengthRanking;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRace();
        result = result * 59 + this.getServerId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqComprehensiveStrengthRanking(race=" + (int)this.getRace() + ", serverId=" + (int)this.getServerId() + ")";
    }
}
