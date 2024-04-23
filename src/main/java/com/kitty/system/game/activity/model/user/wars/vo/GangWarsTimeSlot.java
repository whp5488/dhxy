package com.kitty.system.game.activity.model.user.wars.vo;

import java.io.Serializable;

public class GangWarsTimeSlot implements Serializable {
  public void setSignUpTime(String signUpTime) {
    this.signUpTime = signUpTime;
  }
  
  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }
  
  public void setDayOfWeek(String dayOfWeek) {
    this.dayOfWeek = dayOfWeek;
  }
  
  public void setRound(byte round) {
    this.round = round;
  }
  
  private String signUpTime = "19:40:00";
  
  public String getSignUpTime() {
    return this.signUpTime;
  }
  
  private String startTime = "20:00:00";
  
  public String getStartTime() {
    return this.startTime;
  }
  
  private String dayOfWeek = "3";
  
  public String getDayOfWeek() {
    return this.dayOfWeek;
  }
  
  private byte round = 1;
  
  public byte getRound() {
    return this.round;
  }
}
