package com.kitty.system.game.activity.model.user.at;

import com.kitty.system.init.domain.vo.Param;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AtWillSet {
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
  
  public void setMinMonsterScorePK(int minMonsterScorePK) {
    this.minMonsterScorePK = minMonsterScorePK;
  }
  
  public void setScorePkWin(short scorePkWin) {
    this.scorePkWin = scorePkWin;
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
  
  private int minLevel = 30;
  
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
  
  private int minMonsterScorePK = 0;
  
  public int getMinMonsterScorePK() {
    return this.minMonsterScorePK;
  }
  
  private short scorePkWin = 10;
  
  public short getScorePkWin() {
    return this.scorePkWin;
  }
  
  private int sustain = 150;
  
  public int getSustain() {
    return this.sustain;
  }
  
  private short pkValue = 5;
  
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
