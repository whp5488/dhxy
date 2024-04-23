package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12219)
public class RespBuyResult extends Message {
  private short result;
  
  private String barcode;
  
  public void setResult(short result) {
    this.result = result;
  }
  
  public void setBarcode(String barcode) {
    this.barcode = barcode;
  }
  
  public short getResult() {
    return this.result;
  }
  
  public String getBarcode() {
    return this.barcode;
  }
  
  public RespBuyResult(short result, String barcode) {
    this.result = result;
    this.barcode = barcode;
  }
}
