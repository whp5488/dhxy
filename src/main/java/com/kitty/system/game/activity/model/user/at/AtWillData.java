package com.kitty.system.game.activity.model.user.at;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AtWillData {
  public static final int MAP = 4154;
  
  public static final int MAP_X = 60;
  
  public static final int MAP_Y = 35;
  
  public static final byte STAGE_READY = 0;
  
  public static final byte STAGE_PK = 1;
  
  public static final byte STAGE_END = 2;
  
  private byte stage;
  
  public void setStage(byte stage) {
    this.stage = stage;
  }
  
  public void setSignUpTime(Calendar signUpTime) {
    this.signUpTime = signUpTime;
  }
  
  public void setStartTime(Calendar startTime) {
    this.startTime = startTime;
  }
  
  public void setEndTime(Calendar endTime) {
    this.endTime = endTime;
  }
  
  public void setPkDurationTime(int pkDurationTime) {
    this.pkDurationTime = pkDurationTime;
  }
  
  public void setPkDataList(List<AtWillPkData> pkDataList) {
    this.pkDataList = pkDataList;
  }
  
  public byte getStage() {
    return this.stage;
  }
  
  private Calendar signUpTime = null;
  
  public Calendar getSignUpTime() {
    return this.signUpTime;
  }
  
  private Calendar startTime = null;
  
  public Calendar getStartTime() {
    return this.startTime;
  }
  
  private Calendar endTime = null;
  
  public Calendar getEndTime() {
    return this.endTime;
  }
  
  private int pkDurationTime = 5400;
  
  public int getPkDurationTime() {
    return this.pkDurationTime;
  }
  
  private List<AtWillPkData> pkDataList = new ArrayList<>();
  
  public List<AtWillPkData> getPkDataList() {
    return this.pkDataList;
  }
}
