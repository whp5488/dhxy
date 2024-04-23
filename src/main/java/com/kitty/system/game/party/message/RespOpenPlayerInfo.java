package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.model.PartyDeny;
import java.util.List;

@MessageMeta(module = 11833)
public class RespOpenPlayerInfo extends Message {
  private byte flag = 3;
  
  private List<PartyDeny> denyList;
  
  public byte getFlag() {
    return this.flag;
  }
  
  public void setFlag(byte flag) {
    this.flag = flag;
  }
  
  public List<PartyDeny> getDenyList() {
    return this.denyList;
  }
  
  public void setDenyList(List<PartyDeny> denyList) {
    this.denyList = denyList;
  }
}
