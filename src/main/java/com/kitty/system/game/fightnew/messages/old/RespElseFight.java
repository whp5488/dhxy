package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42035)
public class RespElseFight extends Message
{
    private int autoFight;
    private int showDbSpeed;
    
    public RespElseFight() {
        this.autoFight = 0;
        this.showDbSpeed = 0;
    }
    
    public int getAutoFight() {
        return this.autoFight;
    }
    
    public int getShowDbSpeed() {
        return this.showDbSpeed;
    }
    
    public void setAutoFight(final int autoFight) {
        this.autoFight = autoFight;
    }
    
    public void setShowDbSpeed(final int showDbSpeed) {
        this.showDbSpeed = showDbSpeed;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespElseFight)) {
            return false;
        }
        RespElseFight other = (RespElseFight)o;
        return other.canEqual(this) && this.getAutoFight() == other.getAutoFight() && this.getShowDbSpeed() == other.getShowDbSpeed();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespElseFight;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAutoFight();
        result = result * 59 + this.getShowDbSpeed();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespElseFight(autoFight=" + this.getAutoFight() + ", showDbSpeed=" + this.getShowDbSpeed() + ")";
    }
}
