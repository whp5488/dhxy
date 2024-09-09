package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42120)
public class ReqMyRanking extends Message
{
    private byte type;
    private int serial;
    private byte serverId;
    
    public ReqMyRanking() {
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqMyRanking)) {
            return false;
        }
        ReqMyRanking other = (ReqMyRanking)o;
        return other.canEqual(this) && this.getType() == other.getType() && this.getSerial() == other.getSerial() && this.getServerId() == other.getServerId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqMyRanking;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getServerId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqMyRanking(type=" + (int)this.getType() + ", serial=" + this.getSerial() + ", serverId=" + (int)this.getServerId() + ")";
    }
}
