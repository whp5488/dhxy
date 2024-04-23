package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16582)
public class ReqSetStallGoods extends Message {
  private String inventory;
  
  private String price;
  
  private byte type;
  
  private short amount;
  
  public void setInventory(String inventory) {
    this.inventory = inventory;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setAmount(short amount) {
    this.amount = amount;
  }
  
  public String getInventory() {
    return this.inventory;
  }
  
  public String getPrice() {
    return this.price;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public short getAmount() {
    return this.amount;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    sb.append("\"inventory\":\"")
      .append(this.inventory).append('"');
    sb.append(",\"price\":\"")
      .append(this.price).append('"');
    sb.append(",\"type\":")
      .append(this.type);
    sb.append(",\"amount\":")
      .append(this.amount);
    sb.append('}');
    return sb.toString();
  }
}
