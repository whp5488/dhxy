
package com.kitty.system.game.activity.model.user.wars.vo;

import java.io.Serializable;

public class GangWarsTimeSlot implements Serializable
{
    private String signUpTime;
    private String startTime;
    private String dayOfWeek;
    private byte round;
    
    public GangWarsTimeSlot() {
        this.signUpTime = "19:40:00";
        this.startTime = "20:00:00";
        this.dayOfWeek = "3";
        this.round = 1;
    }
    
    public String getSignUpTime() {
        return this.signUpTime;
    }
    
    public String getStartTime() {
        return this.startTime;
    }
    
    public String getDayOfWeek() {
        return this.dayOfWeek;
    }
    
    public byte getRound() {
        return this.round;
    }
    
    public void setSignUpTime(final String signUpTime) {
        this.signUpTime = signUpTime;
    }
    
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }
    
    public void setDayOfWeek(final String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public void setRound(final byte round) {
        this.round = round;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GangWarsTimeSlot)) {
            return false;
        }
        final GangWarsTimeSlot other = (GangWarsTimeSlot)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$signUpTime = this.getSignUpTime();
        final Object other$signUpTime = other.getSignUpTime();
        Label_0065: {
            if (this$signUpTime == null) {
                if (other$signUpTime == null) {
                    break Label_0065;
                }
            }
            else if (this$signUpTime.equals(other$signUpTime)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$startTime = this.getStartTime();
        final Object other$startTime = other.getStartTime();
        Label_0102: {
            if (this$startTime == null) {
                if (other$startTime == null) {
                    break Label_0102;
                }
            }
            else if (this$startTime.equals(other$startTime)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$dayOfWeek = this.getDayOfWeek();
        final Object other$dayOfWeek = other.getDayOfWeek();
        if (this$dayOfWeek == null) {
            if (other$dayOfWeek == null) {
                return this.getRound() == other.getRound();
            }
        }
        else if (this$dayOfWeek.equals(other$dayOfWeek)) {
            return this.getRound() == other.getRound();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GangWarsTimeSlot;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $signUpTime = this.getSignUpTime();
        result = result * 59 + (($signUpTime == null) ? 43 : $signUpTime.hashCode());
        final Object $startTime = this.getStartTime();
        result = result * 59 + (($startTime == null) ? 43 : $startTime.hashCode());
        final Object $dayOfWeek = this.getDayOfWeek();
        result = result * 59 + (($dayOfWeek == null) ? 43 : $dayOfWeek.hashCode());
        result = result * 59 + this.getRound();
        return result;
    }
    
    @Override
    public String toString() {
        return "GangWarsTimeSlot(signUpTime=" + this.getSignUpTime() + ", startTime=" + this.getStartTime() + ", dayOfWeek=" + this.getDayOfWeek() + ", round=" + this.getRound() + ")";
    }
}
