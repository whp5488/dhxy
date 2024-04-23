package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.message.vo.Instruct;
import java.util.List;

@MessageMeta(module = 45556)
public class RespInstruct extends Message {
  private List<Instruct> list;
  
  public void setList(List<Instruct> list) {
    this.list = list;
  }
  
  public List<Instruct> getList() {
    return this.list;
  }
  
  public RespInstruct() {}
  
  public RespInstruct(List<Instruct> list) {
    this.list = list;
  }
}
