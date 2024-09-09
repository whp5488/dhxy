package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45244)
public class RespAtGuessRaceInfo extends Message
{
    private int startTime;
    private int mateSecond;
    
    public RespAtGuessRaceInfo() {
        this.mateSecond = 0;
    }
    
    public int getStartTime() {
        return this.startTime;
    }
    
    public int getMateSecond() {
        return this.mateSecond;
    }
    
    public void setStartTime(final int startTime) {
        this.startTime = startTime;
    }
    
    public void setMateSecond(final int mateSecond) {
        this.mateSecond = mateSecond;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespAtGuessRaceInfo)) {
            return false;
        }
        RespAtGuessRaceInfo other = (RespAtGuessRaceInfo)o;
        return other.canEqual(this) && this.getStartTime() == other.getStartTime() && this.getMateSecond() == other.getMateSecond();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespAtGuessRaceInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getStartTime();
        result = result * 59 + this.getMateSecond();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespAtGuessRaceInfo(startTime=" + this.getStartTime() + ", mateSecond=" + this.getMateSecond() + ")";
    }
}
