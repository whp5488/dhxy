package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 49167)
public class RespGangWarsRoleState extends Message {
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setTime(int time) {
    this.time = time;
  }

  @ConstructorProperties({"state", "time"})
  public RespGangWarsRoleState(byte state, int time) {
    this.state = state;
    this.time = time;
  }
  
  private byte state = 0;
  
  public byte getState() {
    return this.state;
  }
  
  private int time = 0;
  
  public int getTime() {
    return this.time;
  }
  
  public RespGangWarsRoleState() {}
}
