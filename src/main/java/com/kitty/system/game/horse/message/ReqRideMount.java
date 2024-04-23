package com.kitty.system.game.horse.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 4382)
public class ReqRideMount extends Message {
  private String mountId;
  
  private byte type;
  
  public void setMountId(String mountId) {
    this.mountId = mountId;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  @ConstructorProperties({"mountId", "type"})
  public ReqRideMount(String mountId, byte type) {
    this.mountId = mountId;
    this.type = type;
  }
  
  public ReqRideMount() {}
  
  public String getMountId() {
    return this.mountId;
  }
  
  public byte getType() {
    return this.type;
  }
}
