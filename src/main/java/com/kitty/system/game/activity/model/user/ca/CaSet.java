package com.kitty.system.game.activity.model.user.ca;

import com.kitty.system.init.domain.vo.Param;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaSet {
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
  
  public void setWinScore(int winScore) {
    this.winScore = winScore;
  }
  
  public void setFailScore(int failScore) {
    this.failScore = failScore;
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
  
  private int minLevel = 60;
  
  public int getMinLevel() {
    return this.minLevel;
  }
  
  private int minTeamMemberCount = 3;
  
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
  
  private int winScore = 200;
  
  public int getWinScore() {
    return this.winScore;
  }
  
  private int failScore = -50;
  
  public int getFailScore() {
    return this.failScore;
  }
  
  private int sustain = 90;
  
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
}
