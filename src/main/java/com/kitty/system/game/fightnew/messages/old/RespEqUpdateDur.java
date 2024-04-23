package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 222)
public class RespEqUpdateDur extends Message {
  private String id;
  
  private int dur;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setDur(int dur) {
    this.dur = dur;
  }
  
  public String toString() {
    return "RespEqUpdateDur(id=" + getId() + ", dur=" + getDur() + ")";
  }
  
  @ConstructorProperties({"id", "dur"})
  public RespEqUpdateDur(String id, int dur) {
    this.id = id;
    this.dur = dur;
  }
  
  public RespEqUpdateDur() {}
  
  public String getId() {
    return this.id;
  }
  
  public int getDur() {
    return this.dur;
  }
}
