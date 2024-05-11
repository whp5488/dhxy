
package com.kitty.system.game.activity.model.user.lu;

import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.init.domain.vo.Param;
import java.util.Map;
import java.util.List;

public class WaterSet
{
    private int minLevel;
    private int minTeamMemberCount;
    private String signUpTime;
    private String startTime;
    private List<String> dayOfWeek;
    private int score;
    private int feats;
    private int sustain;
    private short pkValue;
    private int period;
    private Map<Byte, List<Param>> award;
    private NutConcurrentMap rankExtraScore;
    
    public WaterSet() {
        this.minLevel = 50;
        this.minTeamMemberCount = 1;
        this.signUpTime = "19:40:00";
        this.startTime = "20:00:00";
        this.dayOfWeek = new ArrayList<String>();
        this.score = 50;
        this.feats = 200;
        this.sustain = 150;
        this.pkValue = 3;
        this.period = 5;
        this.award = new HashMap<Byte, List<Param>>();
        this.rankExtraScore = new NutConcurrentMap();
    }
    
    public void initRankExtraScore() {
        int basics = 160;
        for (int i = 0; i < 16; ++i) {
            this.rankExtraScore.setv(i + 1 + "_SCORE", basics);
            basics -= 10;
        }
        basics = 1200;
        for (int i = 0; i < 12; ++i) {
            this.rankExtraScore.setv(i + 1 + "_FEATS", basics);
            basics -= 100;
        }
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
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setFeats(final int feats) {
        this.feats = feats;
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
    
    public void setRankExtraScore(final NutConcurrentMap rankExtraScore) {
        this.rankExtraScore = rankExtraScore;
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
    
    public int getScore() {
        return this.score;
    }
    
    public int getFeats() {
        return this.feats;
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
    
    public NutConcurrentMap getRankExtraScore() {
        return this.rankExtraScore;
    }
}
