package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8310)
public class ReqSuitSkillEffects extends Message {
  private int serial;
  
  private byte type;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqSuitSkillEffects))
      return false; 
    ReqSuitSkillEffects other = (ReqSuitSkillEffects)o;
    return !other.canEqual(this) ? false : ((getSerial() != other.getSerial()) ? false : (!(getType() != other.getType())));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqSuitSkillEffects;
  }
  
  public int hashCode() {
    int result = 1;
    result = result * 59 + getSerial();
    return result * 59 + getType();
  }
  
  public String toString() {
    return "ReqSuitSkillEffects(serial=" + getSerial() + ", type=" + getType() + ")";
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public byte getType() {
    return this.type;
  }
}
