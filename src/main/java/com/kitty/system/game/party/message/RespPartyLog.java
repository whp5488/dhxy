package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.message.vo.PartyLogInfo;
import java.util.List;

@MessageMeta(module = 9011)
public class RespPartyLog extends Message {
  private List<PartyLogInfo> list;
  
  public void setList(List<PartyLogInfo> list) {
    this.list = list;
  }
  
  public List<PartyLogInfo> getList() {
    return this.list;
  }
}
