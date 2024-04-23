package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11731)
public class RespCombatCommandAccepted extends Message {
  private int id;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setResult(int result) {
    this.result = result;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof RespCombatCommandAccepted))
      return false; 
    RespCombatCommandAccepted other = (RespCombatCommandAccepted)o;
    return !other.canEqual(this) ? false : ((getId() != other.getId()) ? false : (!(getResult() != other.getResult())));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof RespCombatCommandAccepted;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    result = result * 59 + getId();
    return result * 59 + getResult();
  }
  
  public String toString() {
    return "RespCombatCommandAccepted(id=" + getId() + ", result=" + getResult() + ")";
  }
  
  public int getId() {
    return this.id;
  }
  
  private int result = 0;
  
  public int getResult() {
    return this.result;
  }
}
