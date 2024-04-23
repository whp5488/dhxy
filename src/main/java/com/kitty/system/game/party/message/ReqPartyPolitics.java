package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20534)
public class ReqPartyPolitics extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqPartyPolitics))
      return false; 
    ReqPartyPolitics other = (ReqPartyPolitics)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqPartyPolitics;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqPartyPolitics()";
  }
}
