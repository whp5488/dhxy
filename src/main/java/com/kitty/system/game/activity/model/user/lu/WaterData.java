package com.kitty.system.game.activity.model.user.lu;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;

public class WaterData
{
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
    private Calendar signUpTime;
    private Calendar startTime;
    private Calendar endTime;
    private int pkDurationTime;
    private Map<Integer, WaterPkData> pkDataMap;
    private Map<String, List<WaterPkData>> pkDataByLevel;
    private List<WaterPkData> pkDataList;
    private List<Integer> failTeamIds;
    private Map<Integer, Integer> matchTeamIds;
    
    public WaterData() {
        this.signUpTime = null;
        this.startTime = null;
        this.endTime = null;
        this.pkDurationTime = 5400;
        this.pkDataMap = new ConcurrentHashMap();
        this.pkDataByLevel = new ConcurrentHashMap();
        this.pkDataList = new ArrayList<>();
        this.failTeamIds = new ArrayList<>();
        this.matchTeamIds = new HashMap();
    }
    
    public void setStage(final byte stage) {
        this.stage = stage;
    }
    
    public void setSignUpTime(final Calendar signUpTime) {
        this.signUpTime = signUpTime;
    }
    
    public void setStartTime(final Calendar startTime) {
        this.startTime = startTime;
    }
    
    public void setEndTime(final Calendar endTime) {
        this.endTime = endTime;
    }
    
    public void setPkDurationTime(final int pkDurationTime) {
        this.pkDurationTime = pkDurationTime;
    }
    
    public void setPkDataMap(final Map<Integer, WaterPkData> pkDataMap) {
        this.pkDataMap = pkDataMap;
    }
    
    public void setPkDataByLevel(final Map<String, List<WaterPkData>> pkDataByLevel) {
        this.pkDataByLevel = pkDataByLevel;
    }
    
    public void setPkDataList(final List<WaterPkData> pkDataList) {
        this.pkDataList = pkDataList;
    }
    
    public void setFailTeamIds(final List<Integer> failTeamIds) {
        this.failTeamIds = failTeamIds;
    }
    
    public void setMatchTeamIds(final Map<Integer, Integer> matchTeamIds) {
        this.matchTeamIds = matchTeamIds;
    }
    
    public byte getStage() {
        return this.stage;
    }
    
    public Calendar getSignUpTime() {
        return this.signUpTime;
    }
    
    public Calendar getStartTime() {
        return this.startTime;
    }
    
    public Calendar getEndTime() {
        return this.endTime;
    }
    
    public int getPkDurationTime() {
        return this.pkDurationTime;
    }
    
    public Map<Integer, WaterPkData> getPkDataMap() {
        return this.pkDataMap;
    }
    
    public Map<String, List<WaterPkData>> getPkDataByLevel() {
        return this.pkDataByLevel;
    }
    
    public List<WaterPkData> getPkDataList() {
        return this.pkDataList;
    }
    
    public List<Integer> getFailTeamIds() {
        return this.failTeamIds;
    }
    
    public Map<Integer, Integer> getMatchTeamIds() {
        return this.matchTeamIds;
    }
}
