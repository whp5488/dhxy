package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 13143)
public class RespAgentResult extends Message {
  private int auth_key;
  
  public void setAuth_key(int auth_key) {
    this.auth_key = auth_key;
  }
  
  public void setResult(int result) {
    this.result = result;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof RespAgentResult))
      return false; 
    RespAgentResult other = (RespAgentResult)o;
    if (!other.canEqual(this))
      return false; 
    if (getAuth_key() != other.getAuth_key())
      return false; 
    if (getResult() != other.getResult())
      return false; 
    Object this$msg = getMsg(), other$msg = other.getMsg();
    return !((this$msg == null) ? (other$msg != null) : !this$msg.equals(other$msg));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof RespAgentResult;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    result = result * 59 + getAuth_key();
    result = result * 59 + getResult();
    Object $msg = getMsg();
    return result * 59 + (($msg == null) ? 43 : $msg.hashCode());
  }
  
  public String toString() {
    return "RespAgentResult(auth_key=" + getAuth_key() + ", result=" + getResult() + ", msg=" + getMsg() + ")";
  }
  
  public int getAuth_key() {
    return this.auth_key;
  }
  
  private int result = 1;
  
  private String msg;
  
  public int getResult() {
    return this.result;
  }
  
  public String getMsg() {
    return this.msg;
  }
}
