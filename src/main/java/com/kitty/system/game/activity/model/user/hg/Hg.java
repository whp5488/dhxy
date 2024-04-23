package com.kitty.system.game.activity.model.user.hg;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class Hg implements Serializable {
  private long id;
  
  private byte type;
  
  private long time;
  
  public void setId(long id) {
    this.id = id;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setTime(long time) {
    this.time = time;
  }
  
  public void setMap(Map<Long, Byte> map) {
    this.map = map;
  }
  
  public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
    this.scheduledFuture = scheduledFuture;
  }
  
  @ConstructorProperties({"id", "type", "time", "map", "scheduledFuture"})
  public Hg(long id, byte type, long time, Map<Long, Byte> map, ScheduledFuture<?> scheduledFuture) {
    this.id = id;
    this.type = type;
    this.time = time;
    this.map = map;
    this.scheduledFuture = scheduledFuture;
  }
  
  public long getId() {
    return this.id;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public long getTime() {
    return this.time;
  }
  
  private Map<Long, Byte> map = new HashMap<>(5);
  
  private ScheduledFuture<?> scheduledFuture;
  
  public Map<Long, Byte> getMap() {
    return this.map;
  }
  
  public ScheduledFuture<?> getScheduledFuture() {
    return this.scheduledFuture;
  }
  
  public Hg(long id, byte type, long time) {
    this.id = id;
    this.type = type;
    this.time = time;
  }
  
  public Hg() {}
}
