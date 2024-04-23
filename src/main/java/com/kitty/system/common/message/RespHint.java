package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 8866)
public class RespHint extends Message {
  private int type;
  
  private short result;
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setResult(short result) {
    this.result = result;
  }
  
  public RespHint() {
    this.result = 0;
  }
  
  @ConstructorProperties({"type", "result"})
  public RespHint(int type, short result) {
    this.result = 0;
    this.type = type;
    this.result = result;
  }
  
  public int getType() {
    return this.type;
  }
  
  public short getResult() {
    return this.result;
  }
}
