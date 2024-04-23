package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;

public class SandClockParam {
  private int objId;
  
  private int isClear;
  
  public void setObjId(int objId) {
    this.objId = objId;
  }
  
  public void setIsClear(int isClear) {
    this.isClear = isClear;
  }
  
  @ConstructorProperties({"objId", "isClear"})
  public SandClockParam(int objId, int isClear) {
    this.isClear = 1;
    this.objId = objId;
    this.isClear = isClear;
  }
  
  public SandClockParam() {
    this.isClear = 1;
  }
  
  public int getObjId() {
    return this.objId;
  }
  
  public int getIsClear() {
    return this.isClear;
  }
}
