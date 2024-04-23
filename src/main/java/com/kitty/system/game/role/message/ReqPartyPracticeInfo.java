package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 28841)
public class ReqPartyPracticeInfo extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqPartyPracticeInfo))
      return false; 
    ReqPartyPracticeInfo other = (ReqPartyPracticeInfo)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqPartyPracticeInfo;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqPartyPracticeInfo()";
  }
}
