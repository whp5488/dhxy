package com.kitty.system.game.onlinemall.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import java.util.List;

@MessageMeta(module = 65499)
public class RespOnlineMall extends Message {
  public void setType(int type) {
    this.type = type;
  }
  
  public void setList(List<OnlineMallInfo> list) {
    this.list = list;
  }
  
  private int type = 0;
  
  private List<OnlineMallInfo> list;
  
  public int getType() {
    return this.type;
  }
  
  public List<OnlineMallInfo> getList() {
    return this.list;
  }
}
