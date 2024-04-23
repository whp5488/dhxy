package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4228)
public class ReqMaterTeamRanking extends Message {
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqMaterTeamRanking))
      return false; 
    ReqMaterTeamRanking other = (ReqMaterTeamRanking)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqMaterTeamRanking;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqMaterTeamRanking()";
  }
}
