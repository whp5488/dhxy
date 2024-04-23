package com.kitty.system.boss.model;

import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;

public class BossFightParam {
  private List<SysDataMonster> bossParamList;
  
  private int fightType;
  
  private int taskId;
  
  private int npcId;
  
  private short npcLevel;
  
  private List<String> shoutingList;
  
  public void setFightType(int fightType) {
    this.fightType = fightType;
  }
  
  public void setTaskId(int taskId) {
    this.taskId = taskId;
  }
  
  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public void setNpcLevel(short npcLevel) {
    this.npcLevel = npcLevel;
  }
  
  public void setShoutingList(List<String> shoutingList) {
    this.shoutingList = shoutingList;
  }
  
  public void setLoadGuard(boolean loadGuard) {
    this.loadGuard = loadGuard;
  }
  
  public void setSpecialMonsterType(byte specialMonsterType) {
    this.specialMonsterType = specialMonsterType;
  }
  
  public int getFightType() {
    return this.fightType;
  }
  
  public int getTaskId() {
    return this.taskId;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public short getNpcLevel() {
    return this.npcLevel;
  }
  
  public List<String> getShoutingList() {
    return this.shoutingList;
  }
  
  private boolean loadGuard = true;
  
  private byte specialMonsterType;
  
  public boolean isLoadGuard() {
    return this.loadGuard;
  }
  
  public byte getSpecialMonsterType() {
    return this.specialMonsterType;
  }
  
  public BossFightParam(List<SysDataMonster> bossParamList, int fightType) {
    this.bossParamList = bossParamList;
    this.fightType = fightType;
  }
  
  public BossFightParam(List<SysDataMonster> bossParamList, int fightType, int taskId, int npcId) {
    this.bossParamList = bossParamList;
    this.fightType = fightType;
    this.taskId = taskId;
    this.npcId = npcId;
  }
  
  public List<SysDataMonster> getBossParamList() {
    return this.bossParamList;
  }
  
  public void setBossParamList(List<SysDataMonster> bossParamList) {
    this.bossParamList = bossParamList;
  }
}
