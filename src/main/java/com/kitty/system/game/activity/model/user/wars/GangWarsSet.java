package com.kitty.system.game.activity.model.user.wars;

import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsTimeSlot;
import java.util.List;

public class GangWarsSet
{
    private int minLevel;
    private int minTeamMemberCount;
    private List<GangWarsTimeSlot> dayOfWeek;
    private int score;
    private int sustain;
    private int period;
    
    public GangWarsSet() {
        this.minLevel = 50;
        this.minTeamMemberCount = 3;
        this.dayOfWeek = new ArrayList<>();
        this.score = 50;
        this.sustain = 30;
        this.period = 5;
    }
    
    public void setMinLevel(final int minLevel) {
        this.minLevel = minLevel;
    }
    
    public void setMinTeamMemberCount(final int minTeamMemberCount) {
        this.minTeamMemberCount = minTeamMemberCount;
    }
    
    public void setDayOfWeek(final List<GangWarsTimeSlot> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setSustain(final int sustain) {
        this.sustain = sustain;
    }
    
    public void setPeriod(final int period) {
        this.period = period;
    }
    
    public int getMinLevel() {
        return this.minLevel;
    }
    
    public int getMinTeamMemberCount() {
        return this.minTeamMemberCount;
    }
    
    public List<GangWarsTimeSlot> getDayOfWeek() {
        return this.dayOfWeek;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public int getSustain() {
        return this.sustain;
    }
    
    public int getPeriod() {
        return this.period;
    }
    
    @Override
    public String toString() {
        return "GangWarsSet(minLevel=" + this.getMinLevel() + ", minTeamMemberCount=" + this.getMinTeamMemberCount() + ", dayOfWeek=" + this.getDayOfWeek() + ", score=" + this.getScore() + ", sustain=" + this.getSustain() + ", period=" + this.getPeriod() + ")";
    }
}
