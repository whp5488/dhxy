package com.kitty.system.game.mirror.model;

import java.util.List;

public class MirrorFightParam {
  private byte fightType;
  
  private int npcId;
  
  FightMirror fightMirror;
  
  List<FightMirror> fightMirrorList;
  
  public void setFightType(byte fightType) {
    this.fightType = fightType;
  }
  
  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public void setFightMirror(FightMirror fightMirror) {
    this.fightMirror = fightMirror;
  }
  
  public void setFightMirrorList(List<FightMirror> fightMirrorList) {
    this.fightMirrorList = fightMirrorList;
  }
  
  public byte getFightType() {
    return this.fightType;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public FightMirror getFightMirror() {
    return this.fightMirror;
  }
  
  public List<FightMirror> getFightMirrorList() {
    return this.fightMirrorList;
  }
  
  public MirrorFightParam(byte fightType, int npcId, FightMirror fightMirror) {
    this.fightType = fightType;
    this.npcId = npcId;
    this.fightMirror = fightMirror;
  }
  
  public MirrorFightParam(byte fightType, int npcId, List<FightMirror> fightMirrorList) {
    this.fightType = fightType;
    this.npcId = npcId;
    this.fightMirrorList = fightMirrorList;
  }
}
