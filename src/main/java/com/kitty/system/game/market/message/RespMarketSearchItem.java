package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 33054)
public class RespMarketSearchItem extends Message {
  private int serial;
  
  private String price;
  
  private String minimum;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  public void setMinimum(String minimum) {
    this.minimum = minimum;
  }
  
  public RespMarketSearchItem() {}
  
  @ConstructorProperties({"serial", "price", "minimum"})
  public RespMarketSearchItem(int serial, String price, String minimum) {
    this.serial = serial;
    this.price = price;
    this.minimum = minimum;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getPrice() {
    return this.price;
  }
  
  public String getMinimum() {
    return this.minimum;
  }
}
