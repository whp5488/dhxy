package com.kitty.system.game.suit.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4757)
public class ReqSuit extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqSuit))
      return false; 
    ReqSuit other = (ReqSuit)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqSuit;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqSuit()";
  }
}
