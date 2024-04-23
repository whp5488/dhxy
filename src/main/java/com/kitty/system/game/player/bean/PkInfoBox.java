package com.kitty.system.game.player.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PkInfoBox implements Serializable {
  private boolean redName;
  
  private int incense;
  
  private int conceal;
  
  private int redTime;
  
  private int prisonTime;
  
  public void setRedName(boolean redName) {
    this.redName = redName;
  }
  
  public void setIncense(int incense) {
    this.incense = incense;
  }
  
  public void setConceal(int conceal) {
    this.conceal = conceal;
  }
  
  public void setRedTime(int redTime) {
    this.redTime = redTime;
  }
  
  public void setPrisonTime(int prisonTime) {
    this.prisonTime = prisonTime;
  }
  
  public void setKillRoles(Map<Long, PkRecord> killRoles) {
    this.killRoles = killRoles;
  }
  
  public void setBeKillRoles(Map<Long, PkRecord> beKillRoles) {
    this.beKillRoles = beKillRoles;
  }
  
  public boolean isRedName() {
    return this.redName;
  }
  
  public int getIncense() {
    return this.incense;
  }
  
  public int getConceal() {
    return this.conceal;
  }
  
  public int getRedTime() {
    return this.redTime;
  }
  
  public int getPrisonTime() {
    return this.prisonTime;
  }
  
  private Map<Long, PkRecord> killRoles = new HashMap<>();
  
  public Map<Long, PkRecord> getKillRoles() {
    return this.killRoles;
  }
  
  private Map<Long, PkRecord> beKillRoles = new HashMap<>();
  
  public Map<Long, PkRecord> getBeKillRoles() {
    return this.beKillRoles;
  }
}
