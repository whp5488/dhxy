package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8311)
public class ReqSkillEffects extends Message {
  private int serial;
  
  public String toString() {
    return "ReqSkillEffects(serial=" + getSerial() + ")";
  }
  
  public int hashCode() {
    int result = 1;
    return result * 59 + getSerial();
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqSkillEffects;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqSkillEffects))
      return false; 
    ReqSkillEffects other = (ReqSkillEffects)o;
    return !other.canEqual(this) ? false : (!(getSerial() != other.getSerial()));
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public int getSerial() {
    return this.serial;
  }
}
