package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.common.enter.FiedValue;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;

public class ExcuteFightObjectParam implements Serializable {
  private List<Integer> damageType;
  
  private List<Integer> statusList;
  
  private List<Integer> clearStatus;
  
  private List<FiedValue> fiedValues;
  
  public void setDamageType(List<Integer> damageType) {
    this.damageType = damageType;
  }
  
  public void setStatusList(List<Integer> statusList) {
    this.statusList = statusList;
  }
  
  public void setClearStatus(List<Integer> clearStatus) {
    this.clearStatus = clearStatus;
  }
  
  public void setFiedValues(List<FiedValue> fiedValues) {
    this.fiedValues = fiedValues;
  }
  
  @ConstructorProperties({"damageType", "statusList", "clearStatus", "fiedValues"})
  public ExcuteFightObjectParam(List<Integer> damageType, List<Integer> statusList, List<Integer> clearStatus, List<FiedValue> fiedValues) {
    this.damageType = damageType;
    this.statusList = statusList;
    this.clearStatus = clearStatus;
    this.fiedValues = fiedValues;
  }
  
  public ExcuteFightObjectParam() {}
  
  public List<Integer> getDamageType() {
    return this.damageType;
  }
  
  public List<Integer> getStatusList() {
    return this.statusList;
  }
  
  public List<Integer> getClearStatus() {
    return this.clearStatus;
  }
  
  public List<FiedValue> getFiedValues() {
    return this.fiedValues;
  }
}
