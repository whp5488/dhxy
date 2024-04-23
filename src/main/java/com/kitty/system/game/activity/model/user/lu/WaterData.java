package com.kitty.system.game.activity.model.user.lu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class WaterData {
  public static final int MARS = 400001;
  
  public static final int WARRIOR = 400002;
  
  public static final int LEO = 400003;
  
  public static final int PRODIGY = 400004;
  
  public static final int GEEK = 400005;
  
  public static final int ROOKIE = 400006;
  
  public static final int MAP = 3003;
  
  public static final int MAP_X = 60;
  
  public static final int MAP_Y = 35;
  
  public static final byte STAGE_READY = 0;
  
  public static final byte STAGE_PK = 1;
  
  public static final byte STAGE_END = 2;
  
  public static final byte WATER_WAIT_FIGHT_END = 1;
  
  public static final byte WATER_WAIT_FIGHT_PK = 2;
  
  public static final byte WATER_FIGHT_END = 3;
  
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
  
  public void setPkDataMap(Map<Integer, WaterPkData> pkDataMap) {
    this.pkDataMap = pkDataMap;
  }
  
  public void setPkDataByLevel(Map<String, List<WaterPkData>> pkDataByLevel) {
    this.pkDataByLevel = pkDataByLevel;
  }
  
  public void setPkDataList(List<WaterPkData> pkDataList) {
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
  
  private Map<Integer, WaterPkData> pkDataMap = new ConcurrentHashMap<>();
  
  public Map<Integer, WaterPkData> getPkDataMap() {
    return this.pkDataMap;
  }
  
  private Map<String, List<WaterPkData>> pkDataByLevel = new ConcurrentHashMap<>();
  
  public Map<String, List<WaterPkData>> getPkDataByLevel() {
    return this.pkDataByLevel;
  }
  
  private List<WaterPkData> pkDataList = new ArrayList<>();
  
  public List<WaterPkData> getPkDataList() {
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
