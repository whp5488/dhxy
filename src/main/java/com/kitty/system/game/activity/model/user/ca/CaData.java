package com.kitty.system.game.activity.model.user.ca;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.Calendar;

public class CaData
{
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
    private Calendar signUpTime;
    private Calendar startTime;
    private Calendar endTime;
    private int pkDurationTime;
    private List<CaPkData> pkDataList;
    private List<Integer> failTeamIds;
    private Map<Integer, Integer> matchTeamIds;
    
    public CaData() {
        this.signUpTime = null;
        this.startTime = null;
        this.endTime = null;
        this.pkDurationTime = 5400;
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
    
    public void setPkDataList(final List<CaPkData> pkDataList) {
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
    
    public List<CaPkData> getPkDataList() {
        return this.pkDataList;
    }
    
    public List<Integer> getFailTeamIds() {
        return this.failTeamIds;
    }
    
    public Map<Integer, Integer> getMatchTeamIds() {
        return this.matchTeamIds;
    }
}
