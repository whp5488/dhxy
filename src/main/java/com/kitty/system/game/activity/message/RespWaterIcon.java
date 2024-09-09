package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49160)
public class RespWaterIcon extends Message
{
    private byte round;
    private byte currentSession;
    private byte totalSession;
    private short rank;
    private byte state;
    private int time;
    
    public RespWaterIcon() {
        this.round = 1;
        this.currentSession = 1;
        this.totalSession = 3;
        this.rank = 0;
        this.time = 0;
    }
    
    public byte getRound() {
        return this.round;
    }
    
    public byte getCurrentSession() {
        return this.currentSession;
    }
    
    public byte getTotalSession() {
        return this.totalSession;
    }
    
    public short getRank() {
        return this.rank;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    public void setCurrentSession(final byte currentSession) {
        this.currentSession = currentSession;
    }
    
    public void setTotalSession(final byte totalSession) {
        this.totalSession = totalSession;
    }
    
    public void setRank(final short rank) {
        this.rank = rank;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespWaterIcon)) {
            return false;
        }
        RespWaterIcon other = (RespWaterIcon)o;
        return other.canEqual(this) && this.getRound() == other.getRound() && this.getCurrentSession() == other.getCurrentSession() && this.getTotalSession() == other.getTotalSession() && this.getRank() == other.getRank() && this.getState() == other.getState() && this.getTime() == other.getTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespWaterIcon;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRound();
        result = result * 59 + this.getCurrentSession();
        result = result * 59 + this.getTotalSession();
        result = result * 59 + this.getRank();
        result = result * 59 + this.getState();
        result = result * 59 + this.getTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWaterIcon(round=" + (int)this.getRound() + ", currentSession=" + (int)this.getCurrentSession() + ", totalSession=" + (int)this.getTotalSession() + ", rank=" + (int)this.getRank() + ", state=" + (int)this.getState() + ", time=" + this.getTime() + ")";
    }
}
