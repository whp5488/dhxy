package com.kitty.system.game.activity.model.user.at;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;

public class AtWillData
{
    public static final int MAP = 4154;
    public static final int MAP_X = 60;
    public static final int MAP_Y = 35;
    public static final byte STAGE_READY = 0;
    public static final byte STAGE_PK = 1;
    public static final byte STAGE_END = 2;
    private byte stage;
    private Calendar signUpTime;
    private Calendar startTime;
    private Calendar endTime;
    private int pkDurationTime;
    private List<AtWillPkData> pkDataList;
    
    public AtWillData() {
        this.signUpTime = null;
        this.startTime = null;
        this.endTime = null;
        this.pkDurationTime = 5400;
        this.pkDataList = new ArrayList<>();
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
    
    public void setPkDataList(final List<AtWillPkData> pkDataList) {
        this.pkDataList = pkDataList;
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
    
    public List<AtWillPkData> getPkDataList() {
        return this.pkDataList;
    }
}
