package com.kitty.system.game.skill.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8308)
public class ReqLearnSkill extends Message {
  private int id;
  
  private int serial;
  
  private int learnLevel;
  
  public void setId(int id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setLearnLevel(int learnLevel) {
    this.learnLevel = learnLevel;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqLearnSkill))
      return false; 
    ReqLearnSkill other = (ReqLearnSkill)o;
    return !other.canEqual(this) ? false : ((getId() != other.getId()) ? false : ((getSerial() != other.getSerial()) ? false : (!(getLearnLevel() != other.getLearnLevel()))));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqLearnSkill;
  }
  
  public int hashCode() {
    int result = 1;
    result = result * 59 + getId();
    result = result * 59 + getSerial();
    return result * 59 + getLearnLevel();
  }
  
  public String toString() {
    return "ReqLearnSkill(id=" + getId() + ", serial=" + getSerial() + ", learnLevel=" + getLearnLevel() + ")";
  }

  public int getId() {
    return this.id;
  }

  public int getSerial() {
    return this.serial;
  }

  public int getLearnLevel() {
    return this.learnLevel;
  }
}
