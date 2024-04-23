package com.kitty.system.game.house.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 20606)
public class RespFurnitureStore extends Message {
  private byte is;
  
  public void setIs(byte is) {
    this.is = is;
  }
  
  @ConstructorProperties({"is"})
  public RespFurnitureStore(byte is) {
    this.is = is;
  }
  
  public RespFurnitureStore() {}
  
  public byte getIs() {
    return this.is;
  }
}
