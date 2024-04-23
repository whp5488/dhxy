package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11801)
public class ReqPartyFlag extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqPartyFlag))
      return false; 
    ReqPartyFlag other = (ReqPartyFlag)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqPartyFlag;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqPartyFlag()";
  }
}
