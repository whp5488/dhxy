package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28842)
public class RespPartyPracticeInfo extends Message
{
    private short level;
    private short points;
    
    public short getLevel() {
        return this.level;
    }
    
    public short getPoints() {
        return this.points;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setPoints(final short points) {
        this.points = points;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPartyPracticeInfo)) {
            return false;
        }
        RespPartyPracticeInfo other = (RespPartyPracticeInfo)o;
        return other.canEqual(this) && this.getLevel() == other.getLevel() && this.getPoints() == other.getPoints();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartyPracticeInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getPoints();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyPracticeInfo(level=" + (int)this.getLevel() + ", points=" + (int)this.getPoints() + ")";
    }
    
    public RespPartyPracticeInfo() {
        this.level = 0;
        this.points = 0;
    }
    
    public RespPartyPracticeInfo(final short level, final short points) {
        this.level = 0;
        this.points = 0;
        this.level = level;
        this.points = points;
    }
}
