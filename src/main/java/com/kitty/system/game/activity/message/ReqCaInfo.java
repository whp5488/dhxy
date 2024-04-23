package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 49181)
public class ReqCaInfo extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqCaInfo))
      return false; 
    ReqCaInfo other = (ReqCaInfo)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqCaInfo;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqCaInfo()";
  }
}
