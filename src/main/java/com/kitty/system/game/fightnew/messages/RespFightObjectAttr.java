package com.kitty.system.game.fightnew.messages;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.vo.FightObjectAttr;
import java.util.List;

@MessageMeta(module = 4341)
public class RespFightObjectAttr extends Message {
  private List<FightObjectAttr> list;
  
  public void setList(List<FightObjectAttr> list) {
    this.list = list;
  }
  
  public List<FightObjectAttr> getList() {
    return this.list;
  }
}
