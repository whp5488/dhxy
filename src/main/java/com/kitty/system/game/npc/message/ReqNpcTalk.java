package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12344)
public class ReqNpcTalk extends Message {
  private int npcId;
  
  private String msg;
  
  private String para;
  
  public void setNpcId(int npcId) {
    this.npcId = npcId;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setPara(String para) {
    this.para = para;
  }
  
  public int getNpcId() {
    return this.npcId;
  }
  
  public String getMsg() {
    return this.msg;
  }
  
  public String getPara() {
    return this.para;
  }
}
