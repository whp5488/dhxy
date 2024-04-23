package com.kitty.system.game.market.message.vo;

import com.kitty.system.game.market.model.MarketDealRecord;

public class MarketDealVo {
  private int serial;
  
  private String price;
  
  private short amount;
  
  private byte status;
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setPrice(String price) {
    this.price = price;
  }
  
  public void setAmount(short amount) {
    this.amount = amount;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getPrice() {
    return this.price;
  }
  
  public short getAmount() {
    return this.amount;
  }
  
  public byte getStatus() {
    return this.status;
  }
  
  public static MarketDealVo valueOf(MarketDealRecord record) {
    MarketDealVo vo = new MarketDealVo();
    vo.setSerial(record.getSerial());
    vo.setPrice(String.valueOf(record.getPrice()));
    vo.setAmount(record.getAmount());
    vo.setStatus(record.getStatus());
    return vo;
  }
}
