package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import java.util.List;

@MessageMeta(module = 64993)
public class RespCombatAddOpponent extends Message {
  private List<CombatAddObject> list;
  
  public List<CombatAddObject> getList() {
    return this.list;
  }
  
  public void setList(List<CombatAddObject> list) {
    this.list = list;
  }
}
