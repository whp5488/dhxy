package com.kitty.system.game.shop.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 12356)
public class ReqBuyItem extends Message {
  private int shipper;
  
  private int amount;
  
  private int serial;
  
  private byte type;
  
  private String param;

  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof ReqBuyItem))
      return false; 
    ReqBuyItem other = (ReqBuyItem)o;
    if (!other.canEqual(this))
      return false; 
    if (getShipper() != other.getShipper())
      return false; 
    if (getAmount() != other.getAmount())
      return false; 
    if (getSerial() != other.getSerial())
      return false; 
    if (getType() != other.getType())
      return false; 
    Object this$param = getParam(), other$param = other.getParam();
    return !((this$param == null) ? (other$param != null) : !this$param.equals(other$param));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof ReqBuyItem;
  }
  
  public int hashCode() {
    int result = 1;
    result = result * 59 + getShipper();
    result = result * 59 + getAmount();
    result = result * 59 + getSerial();
    result = result * 59 + getType();
    Object $param = getParam();
    return result * 59 + (($param == null) ? 43 : $param.hashCode());
  }
  
  public String toString() {
    return "ReqBuyItem(shipper=" + getShipper() + ", amount=" + getAmount() + ", serial=" + getSerial() + ", type=" + getType() + ", param=" + getParam() + ")";
  }

}
