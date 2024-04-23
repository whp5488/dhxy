package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4224)
public class ReqPractitionersRanking extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqPractitionersRanking))
      return false; 
    ReqPractitionersRanking other = (ReqPractitionersRanking)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqPractitionersRanking;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqPractitionersRanking()";
  }
}
