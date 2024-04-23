package com.kitty.system.game.store.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 65465)
public class ReqExcuteStorePara extends Message {
  private byte type;
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setStoreType(byte storeType) {
    this.storeType = storeType;
  }
  
  public void setPos(String pos) {
    this.pos = pos;
  }
  
  @ConstructorProperties({"type", "storeType", "pos"})
  public ReqExcuteStorePara(byte type, byte storeType, String pos) {
    this.type = type;
    this.storeType = storeType;
    this.pos = pos;
  }
  
  public byte getType() {
    return this.type;
  }
  
  private byte storeType = 0;
  
  private String pos;
  
  public byte getStoreType() {
    return this.storeType;
  }
  
  public String getPos() {
    return this.pos;
  }
  
  public ReqExcuteStorePara() {}
}
