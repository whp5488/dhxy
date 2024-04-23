package com.kitty.system.game.fightnew.spectator.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4241)
public class ReqLookOn extends Message {
  private String objId;
  
  private byte isNo;
  
  public void setObjId(String objId) {
    this.objId = objId;
  }
  
  public void setIsNo(byte isNo) {
    this.isNo = isNo;
  }
  
  public void setA(boolean isA) {
    this.isA = isA;
  }
  
  public String getObjId() {
    return this.objId;
  }
  
  public byte getIsNo() {
    return this.isNo;
  }
  
  private boolean isA = true;
  
  public boolean isA() {
    return this.isA;
  }
}
