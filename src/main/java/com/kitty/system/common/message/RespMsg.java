package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import org.nutz.json.ToJson;

@ToJson
@MessageMeta(module = 8165)
public class RespMsg extends Message {
  private String msg;
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setActive(short active) {
    this.active = active;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  private short active = 0;
  
  public short getActive() {
    return this.active;
  }
  
  public RespMsg(String msg) {
    this.msg = msg;
  }
  
  public RespMsg(String msg, short active) {
    this.msg = msg;
    this.active = active;
  }
  
  public RespMsg() {}
}
