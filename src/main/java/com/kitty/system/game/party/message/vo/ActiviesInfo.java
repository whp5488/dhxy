package com.kitty.system.game.party.message.vo;

public class ActiviesInfo {
  private String name;
  
  private short participation;
  
  private String activeId;
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setParticipation(short participation) {
    this.participation = participation;
  }
  
  public void setActiveId(String activeId) {
    this.activeId = activeId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public short getParticipation() {
    return this.participation;
  }
  
  public String getActiveId() {
    return this.activeId;
  }
}
