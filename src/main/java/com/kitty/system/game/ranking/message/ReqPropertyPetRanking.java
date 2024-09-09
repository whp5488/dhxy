package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4222)
public class ReqPropertyPetRanking extends Message
{
    private byte type;
    private byte serverId;
    
    public ReqPropertyPetRanking() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPropertyPetRanking)) {
            return false;
        }
        ReqPropertyPetRanking other = (ReqPropertyPetRanking)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getServerId() == other.getServerId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPropertyPetRanking;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getServerId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPropertyPetRanking(type=" + (int)this.getType() + ", serverId=" + (int)this.getServerId() + ")";
    }
}
