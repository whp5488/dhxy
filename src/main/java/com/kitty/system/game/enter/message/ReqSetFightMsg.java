package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import org.nutz.json.ToJson;

@ToJson
@MessageMeta(module = 32914)
public class ReqSetFightMsg extends Message {
  private int id;
  
  private String msg;
  
  public int getId() {
    return this.id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
}
