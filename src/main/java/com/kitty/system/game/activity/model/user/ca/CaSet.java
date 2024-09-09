package com.kitty.system.game.activity.model.user.ca;

import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.system.init.domain.vo.Param;
import java.util.Map;
import java.util.List;

public class CaSet
{
    private int minLevel;
    private int minTeamMemberCount;
    private String signUpTime;
    private String startTime;
    private List<String> dayOfWeek;
    private int winScore;
    private int failScore;
    private int sustain;
    private short pkValue;
    private int period;
    private Map<Byte, List<Param>> award;
    
    public CaSet() {
        this.minLevel = 60;
        this.minTeamMemberCount = 3;
        this.signUpTime = "19:40:00";
        this.startTime = "20:00:00";
        this.dayOfWeek = new ArrayList<>();
        this.winScore = 200;
        this.failScore = -50;
        this.sustain = 90;
        this.pkValue = 3;
        this.period = 5;
        this.award = new HashMap();
    }
    
    public void setMinLevel(final int minLevel) {
        this.minLevel = minLevel;
    }
    
    public void setMinTeamMemberCount(final int minTeamMemberCount) {
        this.minTeamMemberCount = minTeamMemberCount;
    }
    
    public void setSignUpTime(final String signUpTime) {
        this.signUpTime = signUpTime;
    }
    
    public void setStartTime(final String startTime) {
        this.startTime = startTime;
    }
    
    public void setDayOfWeek(final List<String> dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    public void setWinScore(final int winScore) {
        this.winScore = winScore;
    }
    
    public void setFailScore(final int failScore) {
        this.failScore = failScore;
    }
    
    public void setSustain(final int sustain) {
        this.sustain = sustain;
    }
    
    public void setPkValue(final short pkValue) {
        this.pkValue = pkValue;
    }
    
    public void setPeriod(final int period) {
        this.period = period;
    }
    
    public void setAward(final Map<Byte, List<Param>> award) {
        this.award = award;
    }
    
    public int getMinLevel() {
        return this.minLevel;
    }
    
    public int getMinTeamMemberCount() {
        return this.minTeamMemberCount;
    }
    
    public String getSignUpTime() {
        return this.signUpTime;
    }
    
    public String getStartTime() {
        return this.startTime;
    }
    
    public List<String> getDayOfWeek() {
        return this.dayOfWeek;
    }
    
    public int getWinScore() {
        return this.winScore;
    }
    
    public int getFailScore() {
        return this.failScore;
    }
    
    public int getSustain() {
        return this.sustain;
    }
    
    public short getPkValue() {
        return this.pkValue;
    }
    
    public int getPeriod() {
        return this.period;
    }
    
    public Map<Byte, List<Param>> getAward() {
        return this.award;
    }
}
