package com.kitty.system.game.activity.model.user.ca.vo;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CaFight implements Serializable {
  private int teamId;
  
  public void setTeamId(int teamId) {
    this.teamId = teamId;
  }
  
  public void setMembers(List<Long> members) {
    this.members = members;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setSession(byte session) {
    this.session = session;
  }
  
  @ConstructorProperties({"teamId", "members", "state", "session"})
  public CaFight(int teamId, List<Long> members, byte state, byte session) {
    this.teamId = teamId;
    this.members = members;
    this.state = state;
    this.session = session;
  }
  
  public int getTeamId() {
    return this.teamId;
  }
  
  private List<Long> members = new ArrayList<>();
  
  public List<Long> getMembers() {
    return this.members;
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  private byte session = 0;
  
  public byte getSession() {
    return this.session;
  }
  
  public CaFight() {}
}
