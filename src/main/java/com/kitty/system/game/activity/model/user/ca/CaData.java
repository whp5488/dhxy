package com.kitty.system.game.activity.model.user.ca;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CaData {
  public static final int CA_MARS = 900001;
  
  public static final int CA_TRI = 900002;
  
  public static final int CA_FOUR = 900003;
  
  public static final int CA_FIVE = 900004;
  
  public static final int CA_TWO = 900005;
  
  public static final int MAP = 3005;
  
  public static final int MAP_X = 107;
  
  public static final int MAP_Y = 61;
  
  public static final byte STAGE_READY = 0;
  
  public static final byte STAGE_PK = 1;
  
  public static final byte STAGE_END = 2;
  
  public static final byte WAIT_FIGHT_END = 1;
  
  public static final byte WAIT_FIGHT_PK = 2;
  
  public static final byte FIGHT_END = 3;
  
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
  
  public void setPkDataList(List<CaPkData> pkDataList) {
    this.pkDataList = pkDataList;
  }
  
  public void setFailTeamIds(List<Integer> failTeamIds) {
    this.failTeamIds = failTeamIds;
  }
  
  public void setMatchTeamIds(Map<Integer, Integer> matchTeamIds) {
    this.matchTeamIds = matchTeamIds;
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
  
  private List<CaPkData> pkDataList = new ArrayList<>();
  
  public List<CaPkData> getPkDataList() {
    return this.pkDataList;
  }
  
  private List<Integer> failTeamIds = new ArrayList<>();
  
  public List<Integer> getFailTeamIds() {
    return this.failTeamIds;
  }
  
  private Map<Integer, Integer> matchTeamIds = new HashMap<>();
  
  public Map<Integer, Integer> getMatchTeamIds() {
    return this.matchTeamIds;
  }
}
