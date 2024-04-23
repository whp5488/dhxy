package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.common.enter.FiedValue;
import java.beans.ConstructorProperties;
import java.util.List;

public class FightObjectAttr {
  private int objId;
  
  private List<FiedValue> attrs;
  
  public void setObjId(int objId) {
    this.objId = objId;
  }
  
  public void setAttrs(List<FiedValue> attrs) {
    this.attrs = attrs;
  }
  
  @ConstructorProperties({"objId", "attrs"})
  public FightObjectAttr(int objId, List<FiedValue> attrs) {
    this.objId = objId;
    this.attrs = attrs;
  }
  
  public FightObjectAttr() {}
  
  public int getObjId() {
    return this.objId;
  }
  
  public List<FiedValue> getAttrs() {
    return this.attrs;
  }
}
