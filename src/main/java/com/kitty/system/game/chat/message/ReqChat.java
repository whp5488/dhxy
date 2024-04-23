package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.chat.message.vo.ChatInfo;
import java.util.List;

@MessageMeta(module = 16482)
public class ReqChat extends Message {
  private int type;
  
  private String msg;
  
  private List<ChatInfo> list;
  
  private String token;
  
  private String para;
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setList(List<ChatInfo> list) {
    this.list = list;
  }
  
  public void setToken(String token) {
    this.token = token;
  }
  
  public void setPara(String para) {
    this.para = para;
  }
  
  public int getType() {
    return this.type;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public List<ChatInfo> getList() {
    return this.list;
  }
  
  public String getToken() {
    return this.token;
  }
  
  public String getPara() {
    return this.para;
  }
}
