package com.kitty.system.game.activity.model.user.wars.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

public class Equipment implements Serializable {
  private ScheduledFuture<?> scheduledFuture;
  
  private byte stance;
  
  private int team;
  
  private String name;
  
  private List<Long> list;
  
  public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
    this.scheduledFuture = scheduledFuture;
  }
  
  public void setStance(byte stance) {
    this.stance = stance;
  }
  
  public void setTeam(int team) {
    this.team = team;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setList(List<Long> list) {
    this.list = list;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  @ConstructorProperties({"scheduledFuture", "stance", "team", "name", "list", "time"})
  public Equipment(ScheduledFuture<?> scheduledFuture, byte stance, int team, String name, List<Long> list, int time) {
    this.scheduledFuture = scheduledFuture;
    this.stance = stance;
    this.team = team;
    this.name = name;
    this.list = list;
    this.time = time;
  }
  
  public ScheduledFuture<?> getScheduledFuture() {
    return this.scheduledFuture;
  }
  
  public byte getStance() {
    return this.stance;
  }
  
  public int getTeam() {
    return this.team;
  }
  
  public String getName() {
    return this.name;
  }
  
  public List<Long> getList() {
    return this.list;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
  
  public Equipment() {}
}
