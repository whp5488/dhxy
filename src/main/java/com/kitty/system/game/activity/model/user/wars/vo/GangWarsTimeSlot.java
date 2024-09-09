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
        GangWarsTimeSlot other = (GangWarsTimeSlot)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$signUpTime = this.getSignUpTime();
        Object other$signUpTime = other.getSignUpTime();
        if (!((this$signUpTime != null) ? this$signUpTime.equals(other$signUpTime) : (other$signUpTime == null))) {
            return false;
        }
        Object this$startTime = this.getStartTime();
        Object other$startTime = other.getStartTime();
        if (!((this$startTime != null) ? this$startTime.equals(other$startTime) : (other$startTime == null))) {
            return false;
        }
        Object this$dayOfWeek = this.getDayOfWeek();
        Object other$dayOfWeek = other.getDayOfWeek();
        return ((this$dayOfWeek != null) ? this$dayOfWeek.equals(other$dayOfWeek) : (other$dayOfWeek == null)) && this.getRound() == other.getRound();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GangWarsTimeSlot;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $signUpTime = this.getSignUpTime();
        result = result * 59 + (($signUpTime == null) ? 43 : $signUpTime.hashCode());
        Object $startTime = this.getStartTime();
        result = result * 59 + (($startTime == null) ? 43 : $startTime.hashCode());
        Object $dayOfWeek = this.getDayOfWeek();
        result = result * 59 + (($dayOfWeek == null) ? 43 : $dayOfWeek.hashCode());
        result = result * 59 + this.getRound();
        return result;
    }
    
    @Override
    public String toString() {
        return "GangWarsTimeSlot(signUpTime=" + this.getSignUpTime() + ", startTime=" + this.getStartTime() + ", dayOfWeek=" + this.getDayOfWeek() + ", round=" + (int)this.getRound() + ")";
    }
}
