package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import org.nutz.json.ToJson;

@ToJson
@MessageMeta(module = 32913)
public class RespFightMsgDlg extends Message {
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
