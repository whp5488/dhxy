package com.kitty.system.game.pk.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45229)
public class ReqTraceGo extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqTraceGo))
      return false; 
    ReqTraceGo other = (ReqTraceGo)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqTraceGo;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqTraceGo()";
  }
}
