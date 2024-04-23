package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.model.JoinMemberInfo;
import java.util.List;

@MessageMeta(module = 20468)
public class RespJoinPartyList extends Message {
  public void setType(String type) {
    this.type = type;
  }
  
  public void setList(List<JoinMemberInfo> list) {
    this.list = list;
  }
  
  private String type = "party";
  
  private List<JoinMemberInfo> list;
  
  public String getType() {
    return this.type;
  }
  
  public List<JoinMemberInfo> getList() {
    return this.list;
  }
}
