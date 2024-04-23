package com.kitty.system.game.fightRecord.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FightObjectInfo implements Serializable {
  public void setPetMap(Map<Integer, List<Integer>> petMap) {
    this.petMap = petMap;
  }
  
  public void setStatusNumMap(Map<Integer, Map<Integer, Map<Integer, Integer>>> statusNumMap) {
    this.statusNumMap = statusNumMap;
  }
  
  @ConstructorProperties({"petMap", "statusNumMap"})
  public FightObjectInfo(Map<Integer, List<Integer>> petMap, Map<Integer, Map<Integer, Map<Integer, Integer>>> statusNumMap) {
    this.petMap = petMap;
    this.statusNumMap = statusNumMap;
  }
  
  private Map<Integer, List<Integer>> petMap = new HashMap<>();
  
  public Map<Integer, List<Integer>> getPetMap() {
    return this.petMap;
  }
  
  private Map<Integer, Map<Integer, Map<Integer, Integer>>> statusNumMap = new HashMap<>();
  
  public Map<Integer, Map<Integer, Map<Integer, Integer>>> getStatusNumMap() {
    return this.statusNumMap;
  }
  
  public FightObjectInfo() {}
}
