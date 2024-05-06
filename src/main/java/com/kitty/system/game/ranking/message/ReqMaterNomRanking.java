package com.kitty.system.game.ranking.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4227)
public class ReqMaterNomRanking extends Message {

  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqMaterNomRanking))
      return false; 
    ReqMaterNomRanking other = (ReqMaterNomRanking)o;
    return !!other.canEqual(this);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqMaterNomRanking;
  }
  
  public int hashCode() {
    return 1;
  }
  
  public String toString() {
    return "ReqMaterNomRanking()";
  }
}
