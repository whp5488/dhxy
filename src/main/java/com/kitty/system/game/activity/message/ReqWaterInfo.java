package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49161)
public class ReqWaterInfo extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqWaterInfo))
      return false; 
    ReqWaterInfo other = (ReqWaterInfo)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqWaterInfo;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqWaterInfo()";
  }
}
