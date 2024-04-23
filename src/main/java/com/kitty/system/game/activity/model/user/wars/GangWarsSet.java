package com.kitty.system.game.activity.model.user.wars;

import com.kitty.system.game.activity.model.user.wars.vo.GangWarsTimeSlot;
import java.util.ArrayList;
import java.util.List;

public class GangWarsSet {
  public void setMinLevel(int minLevel) {
    this.minLevel = minLevel;
  }
  
  public void setMinTeamMemberCount(int minTeamMemberCount) {
    this.minTeamMemberCount = minTeamMemberCount;
  }
  
  public void setDayOfWeek(List<GangWarsTimeSlot> dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setSustain(int sustain) {
    this.sustain = sustain;
  }
  
  public void setPeriod(int period) {
    this.period = period;
  }
  
  public String toString() {
    return "GangWarsSet(minLevel=" + getMinLevel() + ", minTeamMemberCount=" + getMinTeamMemberCount() + ", dayOfWeek=" + getDayOfWeek() + ", score=" + getScore() + ", sustain=" + getSustain() + ", period=" + getPeriod() + ")";
  }
  
  private int minLevel = 50;
  
  public int getMinLevel() {
    return this.minLevel;
  }
  
  private int minTeamMemberCount = 3;
  
  public int getMinTeamMemberCount() {
    return this.minTeamMemberCount;
  }
  
  private List<GangWarsTimeSlot> dayOfWeek = new ArrayList<>();
  
  public List<GangWarsTimeSlot> getDayOfWeek() {
    return this.dayOfWeek;
  }
  
  private int score = 50;
  
  public int getScore() {
    return this.score;
  }
  
  private int sustain = 30;
  
  public int getSustain() {
    return this.sustain;
  }
  
  private int period = 5;
  
  public int getPeriod() {
    return this.period;
  }
}
