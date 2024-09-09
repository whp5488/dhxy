package com.kitty.system.game.activity.model.user.wars;

import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsInfo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Calendar;

public class GangWarsData
{
    private byte stage;
    private Calendar signUpTime;
    private Calendar startTime;
    private Calendar endTime;
    private ArrayList<String> announce;
    private ConcurrentHashMap<Integer, Map<Long, GangWarsPkData>> pkData;
    private ConcurrentHashMap<Integer, GangWarsInfo> warsMap;
    private NutConcurrentMap recordMap;
    private ConcurrentHashMap<Integer, Integer> byeMap;
    private ConcurrentHashMap<Integer, Equipment> cannonMap;
    private ConcurrentHashMap<Integer, Equipment> gateMap;
    
    public GangWarsData() {
        this.signUpTime = null;
        this.startTime = null;
        this.endTime = null;
        this.announce = new ArrayList<>();
        this.pkData = new ConcurrentHashMap();
        this.warsMap = new ConcurrentHashMap();
        this.recordMap = new NutConcurrentMap();
        this.byeMap = new ConcurrentHashMap();
        this.cannonMap = new ConcurrentHashMap();
        this.gateMap = new ConcurrentHashMap();
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
    
    public void setAnnounce(final ArrayList<String> announce) {
        this.announce = announce;
    }
    
    public void setPkData(final ConcurrentHashMap<Integer, Map<Long, GangWarsPkData>> pkData) {
        this.pkData = pkData;
    }
    
    public void setWarsMap(final ConcurrentHashMap<Integer, GangWarsInfo> warsMap) {
        this.warsMap = warsMap;
    }
    
    public void setRecordMap(final NutConcurrentMap recordMap) {
        this.recordMap = recordMap;
    }
    
    public void setByeMap(final ConcurrentHashMap<Integer, Integer> byeMap) {
        this.byeMap = byeMap;
    }
    
    public void setCannonMap(final ConcurrentHashMap<Integer, Equipment> cannonMap) {
        this.cannonMap = cannonMap;
    }
    
    public void setGateMap(final ConcurrentHashMap<Integer, Equipment> gateMap) {
        this.gateMap = gateMap;
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
    
    public ArrayList<String> getAnnounce() {
        return this.announce;
    }
    
    public ConcurrentHashMap<Integer, Map<Long, GangWarsPkData>> getPkData() {
        return this.pkData;
    }
    
    public ConcurrentHashMap<Integer, GangWarsInfo> getWarsMap() {
        return this.warsMap;
    }
    
    public NutConcurrentMap getRecordMap() {
        return this.recordMap;
    }
    
    public ConcurrentHashMap<Integer, Integer> getByeMap() {
        return this.byeMap;
    }
    
    public ConcurrentHashMap<Integer, Equipment> getCannonMap() {
        return this.cannonMap;
    }
    
    public ConcurrentHashMap<Integer, Equipment> getGateMap() {
        return this.gateMap;
    }
}
