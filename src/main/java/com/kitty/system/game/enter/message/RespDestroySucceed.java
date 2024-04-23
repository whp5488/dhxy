package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 32918)
public class RespDestroySucceed extends Message {
  private Integer type;
  
  private String value;
  
  public void setType(Integer type) {
    this.type = type;
  }
  
  public void setValue(String value) {
    this.value = value;
  }
  
  @ConstructorProperties({"type", "value"})
  public RespDestroySucceed(Integer type, String value) {
    this.type = type;
    this.value = value;
  }
  
  public RespDestroySucceed() {}
  
  public Integer getType() {
    return this.type;
  }
  
  public String getValue() {
    return this.value;
  }
}
