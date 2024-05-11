
package com.kitty.system.game.activity.model.user.at;

import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.system.init.domain.vo.Param;
import java.util.Map;
import java.util.List;

public class AtWillSet
{
    private int minLevel;
    private int minTeamMemberCount;
    private String signUpTime;
    private String startTime;
    private List<String> dayOfWeek;
    private int minMonsterScorePK;
    private short scorePkWin;
    private int sustain;
    private short pkValue;
    private int period;
    private Map<Byte, List<Param>> award;
    
    public AtWillSet() {
        this.minLevel = 30;
        this.minTeamMemberCount = 1;
        this.signUpTime = "19:40:00";
        this.startTime = "20:00:00";
        this.dayOfWeek = new ArrayList<String>();
        this.minMonsterScorePK = 0;
        this.scorePkWin = 10;
        this.sustain = 150;
        this.pkValue = 5;
        this.period = 5;
        this.award = new HashMap<Byte, List<Param>>();
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
    
    public void setMinMonsterScorePK(final int minMonsterScorePK) {
        this.minMonsterScorePK = minMonsterScorePK;
    }
    
    public void setScorePkWin(final short scorePkWin) {
        this.scorePkWin = scorePkWin;
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
    
    public int getMinMonsterScorePK() {
        return this.minMonsterScorePK;
    }
    
    public short getScorePkWin() {
        return this.scorePkWin;
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
