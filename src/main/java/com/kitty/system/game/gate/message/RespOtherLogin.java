package com.kitty.system.game.gate.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45140)
public class RespOtherLogin extends Message {
  public void setResult(Integer result) {
    this.result = result;
  }
  
  public void setCode(Integer code) {
    this.code = code;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof RespOtherLogin))
      return false; 
    RespOtherLogin other = (RespOtherLogin)o;
    if (!other.canEqual(this))
      return false; 
    Object this$result = getResult(), other$result = other.getResult();
    if ((this$result == null) ? (other$result != null) : !this$result.equals(other$result))
      return false; 
    Object this$code = getCode(), other$code = other.getCode();
    if ((this$code == null) ? (other$code != null) : !this$code.equals(other$code))
      return false; 
    Object this$content = getContent(), other$content = other.getContent();
    return !((this$content == null) ? (other$content != null) : !this$content.equals(other$content));
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof RespOtherLogin;
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    Object $result = getResult();
    result = result * 59 + (($result == null) ? 43 : $result.hashCode());
    Object $code = getCode();
    result = result * 59 + (($code == null) ? 43 : $code.hashCode());
    Object $content = getContent();
    return result * 59 + (($content == null) ? 43 : $content.hashCode());
  }
  
  public String toString() {
    return "RespOtherLogin(result=" + getResult() + ", code=" + getCode() + ", content=" + getContent() + ")";
  }
  
  private Integer result = Integer.valueOf(1);
  
  public Integer getResult() {
    return this.result;
  }
  
  private Integer code = Integer.valueOf(9018);
  
  private String content;
  
  public Integer getCode() {
    return this.code;
  }
  
  public String getContent() {
    return this.content;
  }
}
