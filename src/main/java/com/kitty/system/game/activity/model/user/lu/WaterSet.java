package com.kitty.system.game.activity.model.user.lu;

import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.init.domain.vo.Param;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaterSet {
  public void setMinLevel(int minLevel) {
    this.minLevel = minLevel;
  }
  
  public void setMinTeamMemberCount(int minTeamMemberCount) {
    this.minTeamMemberCount = minTeamMemberCount;
  }
  
  public void setSignUpTime(String signUpTime) {
    this.signUpTime = signUpTime;
  }
  
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  
  public void setDayOfWeek(List<String> dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setFeats(int feats) {
    this.feats = feats;
  }
  
  public void setSustain(int sustain) {
    this.sustain = sustain;
  }
  
  public void setPkValue(short pkValue) {
    this.pkValue = pkValue;
  }
  
  public void setPeriod(int period) {
    this.period = period;
  }
  
  public void setAward(Map<Byte, List<Param>> award) {
    this.award = award;
  }
  
  public void setRankExtraScore(NutConcurrentMap rankExtraScore) {
    this.rankExtraScore = rankExtraScore;
  }
  
  private int minLevel = 50;
  
  public int getMinLevel() {
    return this.minLevel;
  }
  
  private int minTeamMemberCount = 1;
  
  public int getMinTeamMemberCount() {
    return this.minTeamMemberCount;
  }
  
  private String signUpTime = "19:40:00";
  
  public String getSignUpTime() {
    return this.signUpTime;
  }
  
  private String startTime = "20:00:00";
  
  public String getStartTime() {
    return this.startTime;
  }
  
  private List<String> dayOfWeek = new ArrayList<>();
  
  public List<String> getDayOfWeek() {
    return this.dayOfWeek;
  }
  
  private int score = 50;
  
  public int getScore() {
    return this.score;
  }
  
  private int feats = 200;
  
  public int getFeats() {
    return this.feats;
  }
  
  private int sustain = 150;
  
  public int getSustain() {
    return this.sustain;
  }
  
  private short pkValue = 3;
  
  public short getPkValue() {
    return this.pkValue;
  }
  
  private int period = 5;
  
  public int getPeriod() {
    return this.period;
  }
  
  private Map<Byte, List<Param>> award = new HashMap<>();
  
  public Map<Byte, List<Param>> getAward() {
    return this.award;
  }
  
  private NutConcurrentMap rankExtraScore = new NutConcurrentMap();
  
  public NutConcurrentMap getRankExtraScore() {
    return this.rankExtraScore;
  }
  
  public void initRankExtraScore() {
    int basics = 160;
    int i;
    for (i = 0; i < 16; i++) {
      this.rankExtraScore.setv((i + 1) + "_SCORE", Integer.valueOf(basics));
      basics -= 10;
    } 
    basics = 1200;
    for (i = 0; i < 12; i++) {
      this.rankExtraScore.setv((i + 1) + "_FEATS", Integer.valueOf(basics));
      basics -= 100;
    } 
  }
}
