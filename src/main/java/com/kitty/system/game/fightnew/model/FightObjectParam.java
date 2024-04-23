package com.kitty.system.game.fightnew.model;

import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FightObjectParam {
  private int type;
  
  private int objId;
  
  private int changeLife;
  
  private int changeMana;
  
  private int currentLife;
  
  private int currentMana;
  
  private int maxLife;
  
  private int maxMana;
  
  private Map<Integer, Integer> statusMap;
  
  private List<Integer> clearStatus;
  
  private int specialStatus;
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setObjId(int objId) {
    this.objId = objId;
  }
  
  public void setChangeLife(int changeLife) {
    this.changeLife = changeLife;
  }
  
  public void setChangeMana(int changeMana) {
    this.changeMana = changeMana;
  }
  
  public void setCurrentLife(int currentLife) {
    this.currentLife = currentLife;
  }
  
  public void setCurrentMana(int currentMana) {
    this.currentMana = currentMana;
  }
  
  public void setMaxLife(int maxLife) {
    this.maxLife = maxLife;
  }
  
  public void setMaxMana(int maxMana) {
    this.maxMana = maxMana;
  }
  
  public void setStatusMap(Map<Integer, Integer> statusMap) {
    this.statusMap = statusMap;
  }
  
  public void setClearStatus(List<Integer> clearStatus) {
    this.clearStatus = clearStatus;
  }
  
  public void setSpecialStatus(int specialStatus) {
    this.specialStatus = specialStatus;
  }
  
  public void setHit(boolean isHit) {
    this.isHit = isHit;
  }
  
  public void setDamageType(List<Integer> damageType) {
    this.damageType = damageType;
  }
  
  public void setMissed(int missed) {
    this.missed = missed;
  }
  
  public void setFightShouting(RespFightShouting fightShouting) {
    this.fightShouting = fightShouting;
  }
  
  public void setDisappear(int disappear) {
    this.disappear = disappear;
  }
  
  public void setGuardTarget(int guardTarget) {
    this.guardTarget = guardTarget;
  }
  
  public void setOrder(int order) {
    this.order = order;
  }
  
  @ConstructorProperties({"type", "objId", "changeLife", "changeMana", "currentLife", "currentMana", "maxLife", "maxMana", "statusMap", "clearStatus", "specialStatus", "isHit", "damageType", "missed", "fightShouting", "disappear", "guardTarget", "order"})
  public FightObjectParam(int type, int objId, int changeLife, int changeMana, int currentLife, int currentMana, int maxLife, int maxMana, Map<Integer, Integer> statusMap, List<Integer> clearStatus, int specialStatus, boolean isHit, List<Integer> damageType, int missed, RespFightShouting fightShouting, int disappear, int guardTarget, int order) {
    this.type = type;
    this.objId = objId;
    this.changeLife = changeLife;
    this.changeMana = changeMana;
    this.currentLife = currentLife;
    this.currentMana = currentMana;
    this.maxLife = maxLife;
    this.maxMana = maxMana;
    this.statusMap = statusMap;
    this.clearStatus = clearStatus;
    this.specialStatus = specialStatus;
    this.isHit = isHit;
    this.damageType = damageType;
    this.missed = missed;
    this.fightShouting = fightShouting;
    this.disappear = disappear;
    this.guardTarget = guardTarget;
    this.order = order;
  }
  
  public int getType() {
    return this.type;
  }
  
  public int getObjId() {
    return this.objId;
  }
  
  public int getChangeLife() {
    return this.changeLife;
  }
  
  public int getChangeMana() {
    return this.changeMana;
  }
  
  public int getCurrentLife() {
    return this.currentLife;
  }
  
  public int getCurrentMana() {
    return this.currentMana;
  }
  
  public int getMaxLife() {
    return this.maxLife;
  }
  
  public int getMaxMana() {
    return this.maxMana;
  }
  
  public List<Integer> getClearStatus() {
    return this.clearStatus;
  }
  
  public int getSpecialStatus() {
    return this.specialStatus;
  }
  
  private boolean isHit = true;
  
  private List<Integer> damageType;
  
  public boolean isHit() {
    return this.isHit;
  }
  
  public List<Integer> getDamageType() {
    return this.damageType;
  }
  
  private int missed = -1;
  
  private RespFightShouting fightShouting;
  
  private int disappear;
  
  private int guardTarget;
  
  private int order;
  
  public int getMissed() {
    return this.missed;
  }
  
  public RespFightShouting getFightShouting() {
    return this.fightShouting;
  }
  
  public int getDisappear() {
    return this.disappear;
  }
  
  public int getGuardTarget() {
    return this.guardTarget;
  }
  
  public int getOrder() {
    return this.order;
  }
  
  public void addClearStatus(int status) {
    if (status == 0)
      return; 
    if (this.clearStatus == null)
      this.clearStatus = new ArrayList<>(); 
    this.clearStatus.add(Integer.valueOf(status));
  }
  
  public void addClearStatus(List<Integer> status) {
    if (status == null || status.size() <= 0)
      return; 
    if (this.clearStatus == null)
      this.clearStatus = new ArrayList<>(); 
    this.clearStatus.addAll(status);
  }
  
  public Map<Integer, Integer> getStatusMap() {
    if (this.statusMap == null)
      this.statusMap = new HashMap<>(); 
    return this.statusMap;
  }
  
  public void addDamageType(int flag) {
    if (this.damageType == null)
      this.damageType = new ArrayList<>(); 
    this.damageType.add(Integer.valueOf(flag));
  }
  
  public FightObjectParam() {}
}
