package com.kitty.system.game.activity.model.user.wars;

import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.game.activity.model.user.wars.vo.Equipment;
import com.kitty.system.game.activity.model.user.wars.vo.GangWarsInfo;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GangWarsData {
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
  
  public void setAnnounce(ArrayList<String> announce) {
    this.announce = announce;
  }
  
  public void setPkData(ConcurrentHashMap<Integer, Map<Long, GangWarsPkData>> pkData) {
    this.pkData = pkData;
  }
  
  public void setWarsMap(ConcurrentHashMap<Integer, GangWarsInfo> warsMap) {
    this.warsMap = warsMap;
  }
  
  public void setRecordMap(NutConcurrentMap recordMap) {
    this.recordMap = recordMap;
  }
  
  public void setByeMap(ConcurrentHashMap<Integer, Integer> byeMap) {
    this.byeMap = byeMap;
  }
  
  public void setCannonMap(ConcurrentHashMap<Integer, Equipment> cannonMap) {
    this.cannonMap = cannonMap;
  }
  
  public void setGateMap(ConcurrentHashMap<Integer, Equipment> gateMap) {
    this.gateMap = gateMap;
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
  
  private ArrayList<String> announce = new ArrayList<>();
  
  public ArrayList<String> getAnnounce() {
    return this.announce;
  }
  
  private ConcurrentHashMap<Integer, Map<Long, GangWarsPkData>> pkData = new ConcurrentHashMap<>();
  
  public ConcurrentHashMap<Integer, Map<Long, GangWarsPkData>> getPkData() {
    return this.pkData;
  }
  
  private ConcurrentHashMap<Integer, GangWarsInfo> warsMap = new ConcurrentHashMap<>();
  
  public ConcurrentHashMap<Integer, GangWarsInfo> getWarsMap() {
    return this.warsMap;
  }
  
  private NutConcurrentMap recordMap = new NutConcurrentMap();
  
  public NutConcurrentMap getRecordMap() {
    return this.recordMap;
  }
  
  private ConcurrentHashMap<Integer, Integer> byeMap = new ConcurrentHashMap<>();
  
  public ConcurrentHashMap<Integer, Integer> getByeMap() {
    return this.byeMap;
  }
  
  private ConcurrentHashMap<Integer, Equipment> cannonMap = new ConcurrentHashMap<>();
  
  public ConcurrentHashMap<Integer, Equipment> getCannonMap() {
    return this.cannonMap;
  }
  
  private ConcurrentHashMap<Integer, Equipment> gateMap = new ConcurrentHashMap<>();
  
  public ConcurrentHashMap<Integer, Equipment> getGateMap() {
    return this.gateMap;
  }
}
