package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import java.util.List;

@MessageMeta(module = 65019)
public class RespCombatFriends extends Message {
  private List<FightMember> list;
  
  public List<FightMember> getList() {
    return this.list;
  }
  
  public void setList(List<FightMember> list) {
    this.list = list;
  }
}
