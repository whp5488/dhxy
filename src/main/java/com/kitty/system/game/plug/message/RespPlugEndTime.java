package com.kitty.system.game.plug.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45303)
public class RespPlugEndTime extends Message
{
    private int plugEndTime;
    
    public int getPlugEndTime() {
        return this.plugEndTime;
    }
    
    public void setPlugEndTime(final int plugEndTime) {
        this.plugEndTime = plugEndTime;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPlugEndTime)) {
            return false;
        }
        RespPlugEndTime other = (RespPlugEndTime)o;
        return other.canEqual(this) && this.getPlugEndTime() == other.getPlugEndTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPlugEndTime;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getPlugEndTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPlugEndTime(plugEndTime=" + this.getPlugEndTime() + ")";
    }
    
    public RespPlugEndTime(final int plugEndTime) {
        this.plugEndTime = 0;
        this.plugEndTime = plugEndTime;
    }
    
    public RespPlugEndTime() {
        this.plugEndTime = 0;
    }
}
